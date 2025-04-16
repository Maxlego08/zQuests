package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Action;
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
import fr.maxlego08.quests.loader.BrewQuestLoader;
import fr.maxlego08.quests.loader.CommandQuestLoader;
import fr.maxlego08.quests.loader.CraftQuestLoader;
import fr.maxlego08.quests.loader.CuboidQuestLoader;
import fr.maxlego08.quests.loader.CustomQuestLoader;
import fr.maxlego08.quests.loader.EnchantQuestLoader;
import fr.maxlego08.quests.loader.EntityQuestLoader;
import fr.maxlego08.quests.loader.InventoryContentQuestLoader;
import fr.maxlego08.quests.loader.InventoryOpenQuestLoader;
import fr.maxlego08.quests.loader.JobQuestLoader;
import fr.maxlego08.quests.loader.MaterialQuestLoader;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestLoader extends ZUtils {

    private final QuestsPlugin plugin;
    private final List<QuestActionLoader> loaders = new ArrayList<>();

    public QuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;

        this.loaders.add(new BrewQuestLoader(plugin));
        this.loaders.add(new CuboidQuestLoader(plugin));
        this.loaders.add(new CommandQuestLoader(plugin));
        this.loaders.add(new CustomQuestLoader(plugin));
        this.loaders.add(new CraftQuestLoader(plugin));
        this.loaders.add(new EnchantQuestLoader(plugin));
        this.loaders.add(new EntityQuestLoader(plugin));
        this.loaders.add(new InventoryContentQuestLoader(plugin));
        this.loaders.add(new InventoryOpenQuestLoader(plugin));
        this.loaders.add(new JobQuestLoader(plugin));
        this.loaders.add(new MaterialQuestLoader(plugin));
    }

    public Quest loadQuest(TypedMapAccessor accessor, File file) {

        try {

            QuestType questType = QuestType.valueOf(accessor.getString("type", QuestType.BLOCK_BREAK.name()).toUpperCase());
            String name = accessor.getString("name", "default-quest-name");

            String displayName = accessor.getString("display-name", name);
            String description = accessor.getString("description", "no description");
            Material thumbnail = accessor.contains("thumbnail") ? Material.valueOf(accessor.getString("thumbnail").toUpperCase()) : null;
            long goal = accessor.getLong("goal", 1);
            boolean autoAccept = accessor.getBoolean("auto-accept", false);
            boolean useGlobalRewards = accessor.getBoolean("use-global-rewards", true);
            boolean canChangeFavorite = accessor.getBoolean("can-change-favorite", true);
            boolean isFavorite = accessor.getBoolean("favorite", false);
            boolean isUnique = accessor.getBoolean("unique", false);
            boolean isHidden = accessor.getBoolean("hidden", false);

            int customModelId = accessor.getInt("custom-model-id", accessor.getInt("custom-model-data", getDefaultCustomModelId(name)));

            List<Map<String, Object>> rewardsMap = accessor.contains("rewards") ? (List<Map<String, Object>>) accessor.getList("rewards") : new ArrayList<>();
            List<Action> rewards = this.plugin.getButtonManager().loadActions(rewardsMap, "rewards", file);

            List<Map<String, Object>> startActionsMap = accessor.contains("start-actions") ? (List<Map<String, Object>>) accessor.getList("start-actions") : new ArrayList<>();
            List<Action> startActions = this.plugin.getButtonManager().loadActions(startActionsMap, "start-actions", file);

            List<QuestAction> questActions = new ArrayList<>();
            if (accessor.contains("actions")) {

                List<Map<String, Object>> actionsMap = (List<Map<String, Object>>) accessor.getList("actions");
                actionsMap.forEach(map -> {

                    TypedMapAccessor actionAccessor = new TypedMapAccessor(map);

                    var optionalLoader = this.loaders.stream().filter(loader -> loader.getSupportedTypes().contains(questType)).findFirst();
                    if (optionalLoader.isPresent()) {
                        questActions.add(optionalLoader.get().load(actionAccessor, questType, file));
                    } else {
                        this.plugin.getLogger().severe("No loader found for " + questType + " in file " + file.getAbsolutePath());
                    }
                });
            }

            if (questActions.isEmpty()) {
                switch (questType) {
                    case HATCHING -> questActions.add(new HatchingAction());
                    case ENTITY_DAMAGE -> questActions.add(new EntityDamageAction());
                    case EXPERIENCE_GAIN -> questActions.add(new ExperienceGainAction());
                    case RESURRECT -> questActions.add(new ResurrectAction());
                    case ISLAND -> questActions.add(new IslandAction());
                }
            }

            List<HologramConfiguration> hologramConfiguration =  new ArrayList<>();
            if (accessor.contains("hologram")) {
                var hologramAccessor = new TypedMapAccessor((Map<String, Object>) accessor.getObject("hologram"));
                hologramConfiguration = HologramConfiguration.fromConfiguration(hologramAccessor);
            }

            return new ZQuest(this.plugin, name, questType, displayName, description, thumbnail, goal, autoAccept, rewards, startActions, questActions, useGlobalRewards, canChangeFavorite, isFavorite, customModelId, isUnique, isHidden, hologramConfiguration);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private int getDefaultCustomModelId(String questName) {
        return this.plugin.getQuestManager().getGroup().values().stream().filter(e -> e.getQuestNames().contains(questName)).findFirst().map(QuestsGroup::getDefaultCustomModelId).orElse(0);
    }

}
