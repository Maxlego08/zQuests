package fr.maxlego08.quests.storage;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.ZActiveQuest;
import fr.maxlego08.quests.ZUserQuest;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.storage.StorageManager;
import fr.maxlego08.quests.api.storage.StorageType;
import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.quests.api.storage.dto.ActiveQuestDTO;
import fr.maxlego08.quests.api.storage.dto.CompletedQuestDTO;
import fr.maxlego08.quests.api.storage.dto.PlayerFavoriteConfigurationDTO;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.storage.migrations.ActiveQuestsCreateMigration;
import fr.maxlego08.quests.storage.migrations.CompletedQuestsCreateMigration;
import fr.maxlego08.quests.storage.migrations.PlayerFavoriteConfigurationMigration;
import fr.maxlego08.quests.zcore.utils.GlobalDatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.SqliteConnection;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.database.Schema;
import fr.maxlego08.sarah.logger.JULogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZStorageManager implements StorageManager {

    private final QuestsPlugin plugin;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private final Map<ActiveQuest, Long> lastUpdate = new HashMap<>();
    private final Map<ActiveQuest, ScheduledFuture<?>> scheduledFutures = new HashMap<>();

    private RequestHelper requestHelper;

    public ZStorageManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadDatabase() {

        FileConfiguration configuration = this.plugin.getConfig();
        StorageType storageType = StorageType.valueOf(configuration.getString("storage-type", StorageType.SQLITE.name()).toUpperCase());

        DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(configuration, storageType);
        DatabaseConnection connection = switch (storageType) {
            case SQLITE -> new SqliteConnection(databaseConfiguration, this.plugin.getDataFolder());
            case HIKARICP, MYSQL -> new HikariDatabaseConnection(databaseConfiguration);
        };
        this.requestHelper = new RequestHelper(connection, JULogger.from(plugin.getLogger()));

        if (!connection.isValid()) {
            plugin.getLogger().severe("Unable to connect to database!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            if (storageType == StorageType.SQLITE) {
                plugin.getLogger().info("The database connection is valid! (SQLITE)");
            } else {
                plugin.getLogger().info("The database connection is valid! (" + connection.getDatabaseConfiguration().getHost() + ")");
            }
        }

        MigrationManager.setMigrationTableName("zquests_migrations");
        MigrationManager.setDatabaseConfiguration(databaseConfiguration);

        MigrationManager.registerMigration(new ActiveQuestsCreateMigration());
        MigrationManager.registerMigration(new CompletedQuestsCreateMigration());
        MigrationManager.registerMigration(new PlayerFavoriteConfigurationMigration());

        MigrationManager.execute(connection, JULogger.from(this.plugin.getLogger()));
    }

    private DatabaseConfiguration getDatabaseConfiguration(FileConfiguration configuration, StorageType storageType) {
        GlobalDatabaseConfiguration globalDatabaseConfiguration = new GlobalDatabaseConfiguration(configuration);
        String tablePrefix = globalDatabaseConfiguration.getTablePrefix();
        String host = globalDatabaseConfiguration.getHost();
        int port = globalDatabaseConfiguration.getPort();
        String user = globalDatabaseConfiguration.getUser();
        String password = globalDatabaseConfiguration.getPassword();
        String database = globalDatabaseConfiguration.getDatabase();
        boolean debug = globalDatabaseConfiguration.isDebug();

        return new DatabaseConfiguration(tablePrefix, user, password, port, host, database, debug, storageType == StorageType.SQLITE ? DatabaseType.SQLITE : DatabaseType.MYSQL);
    }

    @Override
    public void upsert(ActiveQuest activeQuest) {
        executor.execute(() -> this.upsertQuest(activeQuest));
    }

    private void upsertQuest(ActiveQuest activeQuest) {
        this.requestHelper.upsert("%prefix%" + Tables.ACTIVE_QUESTS, upsertToSchema(activeQuest));
    }

    private Consumer<Schema> upsertToSchema(ActiveQuest activeQuest) {
        return table -> {
            table.uuid("unique_id", activeQuest.getUniqueId()).primary();
            table.string("name", activeQuest.getQuest().getName()).primary();
            table.bigInt("amount", activeQuest.getAmount());
            table.bool("is_favorite", activeQuest.isFavorite());
            table.bigInt("start_play_time", activeQuest.getStartPlayTime());
        };
    }

    @Override
    public void upsert(List<ActiveQuest> activeQuests) {
        List<Schema> schemas = new ArrayList<>();
        for (ActiveQuest activeQuest : activeQuests) {
            schemas.add(SchemaBuilder.upsert("%prefix%" + Tables.ACTIVE_QUESTS, upsertToSchema(activeQuest)));
        }
        this.requestHelper.upsertMultiple(schemas);
    }

    @Override
    public void insert(UUID uuid, CompletedQuest completedQuest) {
        executor.execute(() -> this.requestHelper.insert("%prefix%" + Tables.COMPLETED_QUESTS, table -> {
            table.uuid("unique_id", uuid);
            table.string("name", completedQuest.quest().getName());
            table.object("completed_at", completedQuest.completedAt());
            table.object("started_at", completedQuest.startedAt());
            table.bool("is_favorite", completedQuest.isFavorite());
            table.bigInt("start_play_time", completedQuest.startPlayTime());
            table.bigInt("complet_play_time", completedQuest.completPlayTime());
        }));
    }

    @Override
    public void delete(ActiveQuest activeQuest) {

        this.lastUpdate.remove(activeQuest);
        var future = this.scheduledFutures.remove(activeQuest);
        if (future != null) future.cancel(true);

        executor.execute(() -> this.requestHelper.delete("%prefix%" + Tables.ACTIVE_QUESTS, table -> {
            table.where("unique_id", activeQuest.getUniqueId());
            table.where("name", activeQuest.getQuest().getName());
        }));
    }

    @Override
    public void delete(UUID uniqueId, CompletedQuest completedQuest) {
        executor.execute(() -> this.requestHelper.delete("%prefix%" + Tables.COMPLETED_QUESTS, table -> {
            table.where("unique_id", uniqueId);
            table.where("name", completedQuest.quest().getName());
        }));
    }

    @Override
    public void upsertPlayerFavoriteQuestConfiguration(UUID uniqueId, int limit, FavoritePlaceholderType favoritePlaceholderType) {
        executor.execute(() -> this.requestHelper.upsert("%prefix%" + Tables.PLAYER_FAVORITE_CONFIGURATION, table -> {
            table.uuid("unique_id", uniqueId).primary();
            table.bigInt("limit", limit);
            table.string("placeholder_type", favoritePlaceholderType.name());
        }));
    }

    @Override
    public void deleteQuest(@NotNull UUID uniqueId, String name) {
        executor.execute(() -> {
            this.requestHelper.delete("%prefix%" + Tables.ACTIVE_QUESTS, table -> table.where("unique_id", uniqueId).where("name", name));
            this.requestHelper.delete("%prefix%" + Tables.COMPLETED_QUESTS, table -> table.where("unique_id", uniqueId).where("name", name));
        });
    }

    @Override
    public void deleteAll(UUID uuid, Runnable runnable) {
        executor.execute(() -> {
            this.requestHelper.delete("%prefix%" + Tables.ACTIVE_QUESTS, table -> table.where("unique_id", uuid));
            this.requestHelper.delete("%prefix%" + Tables.COMPLETED_QUESTS, table -> table.where("unique_id", uuid));
            runnable.run();
        });
    }

    @Override
    public void load(UUID uuid, Consumer<UserQuest> consumer) {
        executor.execute(() -> {

            List<ActiveQuestDTO> activeQuestDTOS = requestHelper.select("%prefix%" + Tables.ACTIVE_QUESTS, ActiveQuestDTO.class, table -> table.where("unique_id", uuid));
            List<CompletedQuestDTO> completedQuestDTOS = requestHelper.select("%prefix%" + Tables.COMPLETED_QUESTS, CompletedQuestDTO.class, table -> table.where("unique_id", uuid));

            List<ActiveQuest> activeQuests = mapDTOsToQuests(activeQuestDTOS, dto -> plugin.getQuestManager().getQuest(dto.name()).map(quest -> new ZActiveQuest(this.plugin, uuid, quest, dto.created_at(), dto.amount(), dto.is_favorite(), dto.start_play_time())).orElse(null));
            List<CompletedQuest> completedQuests = mapDTOsToQuests(completedQuestDTOS, dto -> plugin.getQuestManager().getQuest(dto.name()).map(quest -> new CompletedQuest(quest, dto.completed_at(), dto.started_at(), dto.is_favorite(), dto.start_play_time(), dto.complet_play_time())).orElse(null));
            var playerFavoriteAmountDTO = requestHelper.select("%prefix%" + Tables.PLAYER_FAVORITE_CONFIGURATION, PlayerFavoriteConfigurationDTO.class, table -> table.where("unique_id", uuid)).stream().findFirst().orElse(new PlayerFavoriteConfigurationDTO(uuid, Config.placeholderFavorites.get(FavoritePlaceholderType.LARGE).maxFavorite(), FavoritePlaceholderType.LARGE));

            // activeQuests.removeIf(ActiveQuest::isComplete);
            consumer.accept(new ZUserQuest(uuid, activeQuests, completedQuests, playerFavoriteAmountDTO.limit(), playerFavoriteAmountDTO.placeholder_type()));
        });
    }

    /**
     * Maps a list of {@link T} objects to a list of {@link R} objects, filtering out any null values.
     *
     * @param list   the list of objects to map
     * @param mapper the function to use to map each element of the list
     * @return a list of the mapped objects, with any null values filtered out
     */
    private <T, R> List<R> mapDTOsToQuests(List<T> list, Function<T, R> mapper) {
        return list.stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void softUpsert(ActiveQuest activeQuest) {
        if (!this.lastUpdate.containsKey(activeQuest) || this.lastUpdate.get(activeQuest) + 5000 < System.currentTimeMillis()) {
            this.lastUpdate.put(activeQuest, System.currentTimeMillis());
            ScheduledFuture<?> scheduledFuture = executor.schedule(() -> {

                if (activeQuest.isComplete()) return;

                this.upsertQuest(activeQuest);
                this.scheduledFutures.remove(activeQuest);
            }, 5, TimeUnit.SECONDS);
            this.scheduledFutures.put(activeQuest, scheduledFuture);
        }
    }
}
