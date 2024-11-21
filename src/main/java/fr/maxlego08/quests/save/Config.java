package fr.maxlego08.quests.save;

import fr.maxlego08.quests.api.utils.ProgressBarConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;
    public static ProgressBarConfig progressBar;

    /**
     * static Singleton instance.
     */
    private static volatile Config instance;


    /**
     * Private constructor for singleton.
     */
    private Config() {
    }

    public static Config getInstance() {
        // Double lock for thread safety.
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    public void load(FileConfiguration configuration) {

        enableDebug = configuration.getBoolean("enable-debug", false);
        enableDebugTime = configuration.getBoolean("enable-debug-time", false);
        progressBar = loadProgressBarConfig(configuration, "progress-bar");
    }

    private ProgressBarConfig loadProgressBarConfig(FileConfiguration configuration, String path) {
        String icon = configuration.getString(path + ".icon", "|");
        String notCompletedIcon = configuration.getString(path + ".not-completed-icon", icon);
        String progressColor = configuration.getString(path + ".progress-color", "&a");
        String color = configuration.getString(path + ".color", "&7");
        int size = configuration.getInt(path + ".size", 10);

        return new ProgressBarConfig(icon, notCompletedIcon, progressColor, color, size);
    }

}
