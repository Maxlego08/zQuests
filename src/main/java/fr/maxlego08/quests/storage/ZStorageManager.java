package fr.maxlego08.quests.storage;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.storage.StorageManager;
import fr.maxlego08.quests.api.storage.StorageType;
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

public class ZStorageManager implements StorageManager {

    private final QuestsPlugin plugin;
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
}
