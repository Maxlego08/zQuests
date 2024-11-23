package fr.maxlego08.quests;

import fr.maxlego08.menu.api.enchantment.Enchantments;
import fr.maxlego08.menu.api.enchantment.MenuEnchantment;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.BrewAction;
import fr.maxlego08.quests.actions.EnchantmentAction;
import fr.maxlego08.quests.actions.EntityAction;
import fr.maxlego08.quests.actions.MaterialAction;
import fr.maxlego08.quests.actions.TagAction;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.zcore.utils.TagRegistry;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestLoader {

    private final QuestsPlugin plugin;

    public QuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    public Quest loadQuest(TypedMapAccessor accessor, File file) {

        try {

            QuestType questType = QuestType.valueOf(accessor.getString("type", QuestType.BLOCK_BREAK.name()).toUpperCase());
            String name = accessor.getString("name");
            String displayName = accessor.getString("display-name", name);
            String description = accessor.getString("description", "no description");
            Material thumbnail = accessor.contains("thumbnail") ? Material.valueOf(accessor.getString("thumbnail").toUpperCase()) : null;
            long goal = accessor.getLong("goal");
            boolean autoAccept = accessor.getBoolean("auto-accept", false);

            List<Map<String, Object>> rewardsMap = accessor.contains("rewards") ? (List<Map<String, Object>>) accessor.getList("rewards") : new ArrayList<>();
            List<Action> rewards = plugin.getButtonManager().loadActions(rewardsMap, "quests", file);

            List<QuestAction> questActions = new ArrayList<>();
            List<Map<String, Object>> actionsMap = (List<Map<String, Object>>) accessor.getList("actions");
            actionsMap.forEach(map -> {

                TypedMapAccessor actionAccessor = new TypedMapAccessor(map);

                if (questType.isMaterial()) {

                    if (actionAccessor.contains("material")) {
                        Material material = Material.valueOf(actionAccessor.getString("material").toUpperCase());
                        questActions.add(new MaterialAction(material, questType));
                    } else if (actionAccessor.contains("tag")) {
                        Tag<Material> tag = TagRegistry.getTag(actionAccessor.getString("tag").toUpperCase());
                        if (tag == null) {
                            plugin.getLogger().severe("Impossible to find the tag or material for " + questType + " in file " + file.getAbsolutePath());
                        } else questActions.add(new TagAction(tag, questType));
                    } else {
                        plugin.getLogger().severe("Impossible to find the tag or material for " + questType + " in file " + file.getAbsolutePath());
                    }

                } else if (questType.isEntityType()) {

                    EntityType entityType = EntityType.valueOf(actionAccessor.getString("entity").toUpperCase());
                    questActions.add(new EntityAction(entityType, questType));

                } else if (questType == QuestType.ENCHANT) {

                    Enchantments enchantments = plugin.getInventoryManager().getEnchantments();
                    String enchantmentName = actionAccessor.getString("enchantment");
                    String materialName = actionAccessor.getString("material", null);

                    Material material = materialName == null ? null : Material.valueOf(materialName.toUpperCase());
                    Enchantment enchantment = enchantmentName == null ? null : enchantments.getEnchantments(enchantmentName).map(MenuEnchantment::getEnchantment).orElse(null);
                    int minimumLevel = actionAccessor.getInt("minimum-level", 0);
                    int minimumCost = actionAccessor.getInt("minimum-cost", 0);

                    questActions.add(new EnchantmentAction(material, questType, enchantment, minimumLevel, minimumCost));

                } else if (questType == QuestType.BREW) {

                    String potionName = actionAccessor.getString("potion-type", null);
                    String potionMaterialName = actionAccessor.getString("potion-material", "POTION");
                    String ingredientName = actionAccessor.getString("ingredient", null);

                    PotionType potionType = potionName == null ? null : PotionType.valueOf(potionName.toUpperCase());
                    Material material = ingredientName == null ? null : Material.valueOf(ingredientName.toUpperCase());
                    Material potionMaterial = Material.valueOf(potionMaterialName.toUpperCase());

                    questActions.add(new BrewAction(potionType, questType, potionMaterial, material));
                }
            });

            return new ZQuest(this.plugin, name, questType, displayName, description, thumbnail, goal, autoAccept, rewards, questActions);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

}
