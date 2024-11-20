package fr.maxlego08.quests.storage;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.ZActiveQuest;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.storage.StorageManager;
import fr.maxlego08.quests.api.storage.StorageType;
import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.quests.api.storage.dto.ActiveQuestDTO;
import fr.maxlego08.quests.storage.migrations.ActiveQuestsCreateMigration;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.MySqlConnection;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.SqliteConnection;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.logger.JULogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ZStorageManager implements StorageManager {

    private final QuestsPlugin plugin;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private RequestHelper requestHelper;

    public ZStorageManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadDatabase() {

        FileConfiguration configuration = plugin.getConfig();
        StorageType storageType = StorageType.valueOf(configuration.getString("storage-type", StorageType.SQLITE.name()).toUpperCase());

        String tablePrefix = configuration.getString("database-configuration.table-prefix");
        String host = configuration.getString("database-configuration.host");
        int port = configuration.getInt("database-configuration.port");
        String user = configuration.getString("database-configuration.user");
        String password = configuration.getString("database-configuration.password");
        String database = configuration.getString("database-configuration.database");
        boolean debug = configuration.getBoolean("database-configuration.debug");

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(tablePrefix, user, password, port, host, database, debug, storageType == StorageType.SQLITE ? DatabaseType.SQLITE : DatabaseType.MYSQL);
        DatabaseConnection connection = switch (storageType) {
            case MYSQL -> new MySqlConnection(databaseConfiguration);
            case SQLITE -> new SqliteConnection(databaseConfiguration, this.plugin.getDataFolder());
            case HIKARICP -> new HikariDatabaseConnection(databaseConfiguration);
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

        MigrationManager.execute(connection, JULogger.from(this.plugin.getLogger()));
    }

    @Override
    public void upsert(ActiveQuest activeQuest) {
        executor.execute(() -> this.requestHelper.upsert("%prefix%" + Tables.ACTIVE_QUESTS, table -> {
            table.uuid("unique_id", activeQuest.getUniqueId()).primary();
            table.string("name", activeQuest.getQuest().getName()).primary();
            table.bigInt("amount", activeQuest.getAmount());
        }));
    }

    @Override
    public void delete(ActiveQuest activeQuest) {
        executor.execute(() -> this.requestHelper.delete("%prefix%" + Tables.ACTIVE_QUESTS, table -> {
            table.where("unique_id", activeQuest.getUniqueId());
            table.where("name", activeQuest.getQuest().getName());
        }));
    }

    @Override
    public void load(UUID uuid, Consumer<List<ActiveQuest>> consumer) {
        executor.execute(() -> {
            List<ActiveQuestDTO> activeQuestDTOS = this.requestHelper.select("%prefix%" + Tables.ACTIVE_QUESTS, ActiveQuestDTO.class, table -> table.where("unique_id", uuid));
            consumer.accept(activeQuestDTOS.stream().map(dto -> {
                Optional<Quest> optional = plugin.getQuestManager().getQuest(dto.name());
                if (optional.isPresent()) {
                    Quest quest = optional.get();
                    return new ZActiveQuest(uuid, quest, dto.amount());
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList()));
        });
    }
}
