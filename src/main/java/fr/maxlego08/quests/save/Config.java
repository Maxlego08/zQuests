package fr.maxlego08.quests.save;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.api.utils.EventConfiguration;
import fr.maxlego08.quests.api.utils.PlaceholderFavorite;
import fr.maxlego08.quests.api.utils.ProgressBarConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Config {

    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;
    public static ProgressBarConfig progressBar;
    public static String loreLinePlaceholderActive;
    public static String loreLinePlaceholderComplete;
    public static String mainCommandInventoryName;
    public static Map<Class<?>, EventConfiguration> eventConfigurations = new HashMap<>();
    public static SimpleDateFormat simpleDateFormat;
    public static PlaceholderFavorite placeholderFavorite;
    public static String globalGroupName;
    public static int eventTicks;
    public static int antiBlockPlaceAbuse;

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
        simpleDateFormat = new SimpleDateFormat(configuration.getString("date-format", "dd/MM/yyyy HH:mm:ss"));
        mainCommandInventoryName = configuration.getString("main-command-inventory-name", "quests");
        globalGroupName = configuration.getString("global-group-display-name", "Global");
        antiBlockPlaceAbuse = configuration.getInt("anti-block-place-abuse", 60000);

        loreLinePlaceholderActive = configuration.getString("lore-line-placeholder.active", "%progress-bar% &8- &6%progress%&8/&f%goal% &c✘");
        loreLinePlaceholderComplete = configuration.getString("lore-line-placeholder.complete", "%progress-bar% &8- &6%progress%&8/&f%goal% &a✔");

        placeholderFavorite = new PlaceholderFavorite(
                configuration.getInt("placeholder-favorite.limit", 3),
                configuration.getString("placeholder-favorite.empty", "&cNo favorite quests"),
                configuration.getString("placeholder-favorite.result", "&f%name%\n&e%progress%&8/&6%goal%"),
                configuration.getString("placeholder-favorite.between", "\n")
        );

        this.loadEventConfiguration(configuration);
    }

    private ProgressBarConfig loadProgressBarConfig(FileConfiguration configuration, String path) {
        String icon = configuration.getString(path + ".icon", "|");
        String notCompletedIcon = configuration.getString(path + ".not-completed-icon", icon);
        String progressColor = configuration.getString(path + ".progress-color", "&a");
        String color = configuration.getString(path + ".color", "&7");
        int size = configuration.getInt(path + ".size", 10);

        return new ProgressBarConfig(icon, notCompletedIcon, progressColor, color, size);
    }

    private void loadEventConfiguration(FileConfiguration configuration) {

        eventConfigurations.clear();

        eventTicks = configuration.getInt("event-ticks", 1);

        for (Map<?, ?> map : configuration.getMapList("events")) {
            TypedMapAccessor accessor = new TypedMapAccessor((Map<String, Object>) map);
            String className = accessor.getString("event");
            boolean enabled = accessor.getBoolean("enabled", true);
            boolean updateScoreboard = accessor.getBoolean("update-scoreboard", false);
            boolean updateHologram = accessor.getBoolean("update-hologram", false);

            try {
                Class<?> classz = Class.forName(String.format("fr.maxlego08.quests.api.event.events.%s", className));
                eventConfigurations.put(classz, new EventConfiguration(className, enabled, updateScoreboard, updateHologram));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}
