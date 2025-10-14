package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.requirement.Permissible;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.EntityDamageAction;
import fr.maxlego08.quests.actions.ExperienceGainAction;
import fr.maxlego08.quests.actions.HatchingAction;
import fr.maxlego08.quests.actions.IslandAction;
import fr.maxlego08.quests.actions.ResurrectAction;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.QuestsGroup;
import fr.maxlego08.quests.api.hologram.HologramConfiguration;
import fr.maxlego08.quests.api.waypoint.WayPointConfiguration;
import fr.maxlego08.quests.loader.BrewQuestLoader;
import fr.maxlego08.quests.loader.CommandQuestLoader;
import fr.maxlego08.quests.loader.CuboidQuestLoader;
import fr.maxlego08.quests.loader.CustomQuestLoader;
import fr.maxlego08.quests.loader.EnchantQuestLoader;
import fr.maxlego08.quests.loader.EntityQuestLoader;
import fr.maxlego08.quests.loader.InventoryContentQuestLoader;
import fr.maxlego08.quests.loader.InventoryOpenQuestLoader;
import fr.maxlego08.quests.loader.ItemStackQuestLoader;
import fr.maxlego08.quests.loader.JobQuestLoader;
import fr.maxlego08.quests.loader.MaterialQuestLoader;
import fr.maxlego08.quests.zcore.utils.Colors;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Material;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestLoader extends ZUtils {

    private final QuestsPlugin plugin;
    private final List<QuestActionLoader> loaders = new ArrayList<>();

    public QuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;

        this.loaders.add(new BrewQuestLoader(plugin));
        this.loaders.add(new CuboidQuestLoader(plugin));
        this.loaders.add(new CommandQuestLoader(plugin));
        this.loaders.add(new CustomQuestLoader(plugin));
        this.loaders.add(new ItemStackQuestLoader(plugin));
        this.loaders.add(new EnchantQuestLoader(plugin));
        this.loaders.add(new EntityQuestLoader(plugin));
        this.loaders.add(new InventoryContentQuestLoader(plugin));
        this.loaders.add(new InventoryOpenQuestLoader(plugin));
        this.loaders.add(new JobQuestLoader(plugin));
        this.loaders.add(new MaterialQuestLoader(plugin));
    }

    /**
     * Loads a {@link Quest} from a {@link TypedMapAccessor} and a {@link File}.
     * <p>
     * The {@link TypedMapAccessor} should contain the configuration for the quest.
     * The {@link File} is used to resolve relative paths in the configuration.
     * </p>
     * <p>
     * If the quest is malformed, this method will return {@code null}.
     * </p>
     * The following configuration options are supported:
     * <ul>
     * <li>{@code display-name}: the display name of the quest</li>
     * <li>{@code description}: the description of the quest</li>
     * <li>{@code placeholder-description}: the description to display when the quest is not completed</li>
     * <li>{@code thumbnail}: the material of the item to display as the quest's thumbnail</li>
     * <li>{@code goal}: the amount of times the quest must be completed</li>
     * <li>{@code auto-accept}: whether the quest should be automatically accepted</li>
     * <li>{@code use-global-rewards}: whether the quest should use global rewards</li>
     * <li>{@code can-change-favorite}: whether the quest can be marked as favorite</li>
     * <li>{@code favorite}: whether the quest is marked as favorite</li>
     * <li>{@code unique}: whether the quest can be completed only once</li>
     * <li>{@code hidden}: whether the quest should be hidden</li>
     * <li>{@code rewards}: the rewards to give when the quest is completed</li>
     * <li>{@code start-actions}: the actions to execute when the quest is started</li>
     * <li>{@code permissible-rewards}: the permissible rewards to give when the quest is completed</li>
     * <li>{@code custom-model-id}: the custom model id of the item to display as the quest's thumbnail</li>
     * <li>{@code hologram-configuration}: the hologram configuration for the quest</li>
     * <li>{@code waypoint-configuration}: the waypoint configuration for the quest</li>
     * <li>{@code action-requirements}: the permissibles that the player must fill out so that the actions can be performed</li>
     * </ul>
     * <p>
     * If a configuration option is not specified, a default value will be used.
     * <p>
     * The {@link Quest} will be created with the specified configuration and
     * the {@link QuestsPlugin} that created this loader.
     * <p>
     * If the quest is malformed, this method will return {@code null}.
     *
     * @param accessor the configuration accessor
     * @param file     the file to resolve relative paths
     * @return the loaded quest or {@code null} if the quest is malformed
     */
    public Quest loadQuest(TypedMapAccessor accessor, File file) {
        try {
            QuestType questType = resolveQuestType(accessor);
            String name = getName(accessor);

            String displayName = accessor.getString("display-name", name);
            String description = accessor.getString("description", "no description");
            String placeholderDescription = accessor.getString("placeholder-description", description);

            Material thumbnail = parseThumbnail(accessor);
            long goal = accessor.getLong("goal", 1);
            boolean autoAccept = accessor.getBoolean("auto-accept", false);
            boolean useGlobalRewards = accessor.getBoolean("use-global-rewards", true);
            boolean canChangeFavorite = accessor.getBoolean("can-change-favorite", true);
            boolean isFavorite = accessor.getBoolean("favorite", false);
            boolean isUnique = accessor.getBoolean("unique", false);
            boolean isHidden = accessor.getBoolean("hidden", false);

            int customModelId = resolveCustomModelId(accessor, name);

            List<Action> rewards = loadActionsSection("rewards", accessor, file);
            List<Action> startActions = loadActionsSection("start-actions", accessor, file);
            List<Permissible> permissibleRewards = loadPermissibleSection("permissible-rewards", accessor, file);

            List<QuestAction> questActions = loadQuestActions(accessor, file, questType);
            if (questActions.isEmpty()) {
                questActions = defaultActionsFor(questType);
            }

            List<HologramConfiguration> hologramConfiguration = loadHologram(accessor, name, file);
            WayPointConfiguration waypointConfiguration = loadWaypoint(accessor, name, file);
            List<Permissible> requirements = loadRequirements(accessor, name, file, "action-requirements");
            List<Permissible> forceConditions = loadRequirements(accessor, name, file, "force-conditions");

            return new ZQuest(this.plugin, name, questType, displayName, description, placeholderDescription, //
                    thumbnail, goal, autoAccept, rewards, permissibleRewards, startActions, questActions, //
                    useGlobalRewards, canChangeFavorite, isFavorite, customModelId, isUnique, //
                    isHidden, hologramConfiguration, waypointConfiguration, requirements, forceConditions);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }


    /**
     * Retrieves the {@link QuestType} from the given configuration accessor.
     * If the accessor does not contain the "type" key, the method returns the default quest type
     * {@link QuestType#BLOCK_BREAK}.
     * Otherwise, it retrieves the raw quest type from the accessor and returns it as a
     * {@link QuestType} object.
     *
     * @param accessor the configuration accessor
     * @return the quest type
     */
    private QuestType resolveQuestType(TypedMapAccessor accessor) {
        String raw = accessor.getString("type", QuestType.BLOCK_BREAK.name());
        return QuestType.valueOf(raw.toUpperCase());
    }


    /**
     * Retrieves the name of the quest from the given configuration accessor.
     * If the accessor does not contain the "name" key, the method returns the default quest name
     * "default-quest-name".
     * Otherwise, it retrieves the raw quest name from the accessor and returns it as is.
     *
     * @param accessor the configuration accessor
     * @return the name of the quest
     */
    private String getName(TypedMapAccessor accessor) {
        return accessor.getString("name", "default-quest-name");
    }

    /**
     * Parses the thumbnail material from the given configuration accessor.
     * If the accessor does not contain the "thumbnail" key, the method returns null.
     * Otherwise, it retrieves the raw material name from the accessor and converts
     * it to uppercase before attempting to parse it as a {@link Material}.
     * If the parsing fails, the method returns null.
     *
     * @param accessor the configuration accessor
     * @return the parsed material, or null if the accessor does not contain the thumbnail key
     */
    private Material parseThumbnail(TypedMapAccessor accessor) {
        if (!accessor.contains("thumbnail")) return null;
        String raw = accessor.getString("thumbnail");
        return Material.valueOf(raw.toUpperCase());
    }


    /**
     * Retrieves the custom model ID for the given quest from the configuration.
     * <p>
     * The method first checks for the presence of the "custom-model-id" key in the configuration,
     * and if it does not exist, falls back to the "custom-model-data" key. If neither key exists,
     * it falls back to the default custom model ID for the given quest name.
     *
     * @param accessor the configuration accessor
     * @param name     the name of the quest
     * @return the custom model ID
     */
    private int resolveCustomModelId(TypedMapAccessor accessor, String name) {
        int fallback = getDefaultCustomModelId(name);
        int legacy = accessor.getInt("custom-model-data", fallback);
        return accessor.getInt("custom-model-id", legacy);
    }


    /**
     * Loads a list of actions from the specified section of the configuration.
     * <p>
     * This method retrieves a list of action maps associated with the given key from the configuration
     * accessor. It then uses the button manager to load and return the corresponding actions.
     *
     * @param key      The key in the configuration to retrieve the actions from.
     * @param accessor The configuration accessor providing access to the configuration data.
     * @param file     The file from which the configuration is being loaded.
     * @return A list of actions loaded from the specified section of the configuration.
     */
    private List<Action> loadActionsSection(String key, TypedMapAccessor accessor, File file) {
        var buttonManager = this.plugin.getButtonManager();
        List<Map<String, Object>> raw = accessor.contains(key) ? castList(accessor.getList(key)) : new ArrayList<>();
        return buttonManager.loadActions(raw, key, file);
    }


    /**
     * Loads a list of {@link Permissible} objects from the given configuration accessor and file.
     * The list is retrieved from the configuration accessor using the given key.
     * If the key is not present, an empty list is returned.
     * The list is then passed to the {@link fr.maxlego08.menu.api.ButtonManager} to load the permissibles.
     *
     * @param key      The key to retrieve the list from the configuration accessor.
     * @param accessor The configuration accessor.
     * @param file     The file containing the configuration.
     * @return The list of loaded permissibles.
     */
    private List<Permissible> loadPermissibleSection(String key, TypedMapAccessor accessor, File file) {
        var buttonManager = this.plugin.getButtonManager();
        List<Map<String, Object>> raw = accessor.contains(key) ? castList(accessor.getList(key)) : new ArrayList<>();
        return buttonManager.loadPermissible(raw, key, file);
    }


    /**
     * Loads a list of quest actions from a configuration accessor.
     * This method first checks if the "actions" key is present in the configuration. If it is not, an empty list is returned.
     * Otherwise, the value associated with the key is expected to be a list of maps. Each map is then passed to the
     * {@link QuestActionLoader#load(TypedMapAccessor, QuestType, File)} method of the first {@link QuestActionLoader} that
     * supports the given quest type. If no loader is found, a severe error is logged and the method continues to the next
     * action.
     * The resulting list of quest actions is then returned.
     *
     * @param accessor  The configuration accessor.
     * @param file      The file containing the configuration.
     * @param questType The quest type to load actions for.
     * @return The list of loaded quest actions.
     */
    private List<QuestAction> loadQuestActions(TypedMapAccessor accessor, File file, QuestType questType) {
        if (!accessor.contains("actions")) return new ArrayList<>();

        List<Map<String, Object>> actionsMap = castList(accessor.getList("actions"));
        List<QuestAction> results = new ArrayList<>();
        for (Map<String, Object> map : actionsMap) {
            TypedMapAccessor actionAccessor = new TypedMapAccessor(map);
            Optional<QuestActionLoader> optional = findLoaderFor(questType);
            if (optional.isPresent()) {
                QuestAction result = optional.get().load(actionAccessor, questType, file);
                if (result != null) results.add(result);
                else logSevere("No loader found for " + questType + " in file " + file.getAbsolutePath());
            } else {
                logSevere("No loader found for " + questType + " in file " + file.getAbsolutePath());
            }
        }
        return results;
    }


    /**
     * Find a {@link QuestActionLoader} that supports the given quest type.
     * <p>
     * This method iterates over the list of loaded {@link QuestActionLoader}s and returns the first one that supports the given quest type.
     * If no loader is found, an empty optional is returned.
     *
     * @param questType The quest type to find a loader for.
     * @return An optional containing the loader if found, otherwise an empty optional.
     */
    private Optional<QuestActionLoader> findLoaderFor(QuestType questType) {
        return this.loaders.stream().filter(loader -> loader.getSupportedTypes().contains(questType)).findFirst();
    }

    /**
     * Return a list of default quest actions for the given quest type.
     * The default actions are:
     * <ul>
     *     <li>HATCHING: {@link HatchingAction}</li>
     *     <li>ENTITY_DAMAGE: {@link EntityDamageAction}</li>
     *     <li>EXPERIENCE_GAIN: {@link ExperienceGainAction}</li>
     *     <li>RESURRECT: {@link ResurrectAction}</li>
     *     <li>ISLAND: {@link IslandAction}</li>
     * </ul>
     * For other types, an empty list is returned.
     *
     * @param questType The quest type to return default actions for.
     * @return A list of default actions for the given quest type.
     */
    private List<QuestAction> defaultActionsFor(QuestType questType) {
        List<QuestAction> defaults = new ArrayList<>();
        switch (questType) {
            case HATCHING -> defaults.add(new HatchingAction());
            case ENTITY_DAMAGE -> defaults.add(new EntityDamageAction());
            case EXPERIENCE_GAIN -> defaults.add(new ExperienceGainAction());
            case RESURRECT -> defaults.add(new ResurrectAction());
            case ISLAND -> defaults.add(new IslandAction());
            default -> {
                // no default for other types
            }
        }
        return defaults;
    }


    /**
     * Load a hologram configuration from a configuration accessor.
     * <p>
     * The "hologram" key in the configuration can be either a string or a map.
     * If it is a string, it is the name of a global hologram configuration.
     * If it is a map, it is the configuration for a hologram.
     *
     * @param accessor  The configuration accessor.
     * @param questName The name of the quest.
     * @param file      The file that the configuration accessor is for.
     * @return The hologram configuration.
     */
    private List<HologramConfiguration> loadHologram(TypedMapAccessor accessor, String questName, File file) {
        List<HologramConfiguration> config = new ArrayList<>();
        if (!accessor.contains("hologram")) return config;

        Object hologramObject = accessor.getObject("hologram");
        if (hologramObject instanceof String string) {
            var optional = this.plugin.getHologramManager().getConfiguration(string);
            if (optional.isPresent()) {
                config = optional.get();
            } else {
                logSevere("Invalid hologram configuration for quest " + questName + " in file " + file.getAbsolutePath() + ", global hologram not found");
            }
        } else if (hologramObject instanceof Map<?, ?> map) {
            //noinspection unchecked
            config = HologramConfiguration.fromConfiguration(new TypedMapAccessor((Map<String, Object>) map));
        } else {
            logSevere("Invalid hologram configuration for quest " + questName + " in file " + file.getAbsolutePath());
        }
        return config;
    }

    /**
     * Load waypoint configuration from config.
     *
     * @param accessor  the {@link TypedMapAccessor} of the configuration
     * @param questName the name of the quest
     * @param file      the file of the configuration
     * @return the loaded waypoint configuration, or null if the configuration is invalid
     */
    private WayPointConfiguration loadWaypoint(TypedMapAccessor accessor, String questName, File file) {
        if (!accessor.contains("waypoint")) return null;

        Object object = accessor.getObject("waypoint");
        if (object instanceof String string) {
            var optional = this.plugin.getWayPointManager().getConfiguration(string);
            if (optional.isPresent()) {
                return optional.get();
            }
            logSevere("Invalid waypoint configuration for quest " + questName + " in file " + file.getAbsolutePath() + ", global waypoint not found");
            return null;
        }
        if (object instanceof Map<?, ?> map) {
            //noinspection unchecked
            TypedMapAccessor wp = new TypedMapAccessor((Map<String, Object>) map);
            var location = changeStringLocationToLocation(wp.getString("location"));
            String texture = wp.contains("texture") ? wp.getString("texture") : null;
            Color color = Colors.parseColor(wp.getString("color", "white"));
            return new WayPointConfiguration(location, texture, color);
        }
        logSevere("Invalid waypoint configuration for quest " + questName + " in file " + file.getAbsolutePath());
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castList(List<?> list) {
        if (list == null) return new ArrayList<>();
        return list.stream().filter(Objects::nonNull).map(e -> (Map<String, Object>) e).collect(Collectors.toList());
    }

    /**
     * Log a severe message.
     * This method will log the given message as a severe log message.
     *
     * @param message the message to log
     */
    private void logSevere(String message) {
        this.plugin.getLogger().severe(message);
    }

    /**
     * Get the default custom model ID for a given quest name by looking up the {@link QuestsGroup} that contains the quest name and returning its default custom model ID.
     * If no group is found, returns 0.
     *
     * @param questName the name of the quest
     * @return the default custom model ID
     */
    private int getDefaultCustomModelId(String questName) {
        return this.plugin.getQuestManager().getGroup().values().stream().filter(e -> e.getQuestNames().contains(questName)).findFirst().map(QuestsGroup::getDefaultCustomModelId).orElse(0);
    }

    /**
     * Loads the action requirements for a quest from the given configuration.
     * If the "action-requirements" key is not present in the configuration, an empty list is returned.
     * If the value associated with the key is not a list, a severe error is logged and an empty list is returned.
     *
     * @param accessor  The configuration accessor.
     * @param questName The name of the quest.
     * @param file      The file containing the configuration.
     * @return The list of action requirements.
     */
    private List<Permissible> loadRequirements(TypedMapAccessor accessor, String questName, File file, String path) {

        if (!accessor.contains(path)) return List.of();

        Object object = accessor.getObject(path);
        if (object instanceof List<?> map) {

            return this.plugin.getButtonManager().loadPermissible((List<Map<String, Object>>) map, path, file);
        } else {
            logSevere("Invalid " + path + " for quest " + questName + " in file " + file.getAbsolutePath());
            return List.of();
        }
    }

}
