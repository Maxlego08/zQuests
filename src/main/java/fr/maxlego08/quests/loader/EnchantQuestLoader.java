package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.enchantment.Enchantments;
import fr.maxlego08.menu.api.enchantment.MenuEnchantment;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.EnchantmentAction;
import fr.maxlego08.quests.actions.ItemStackAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.util.List;

public class EnchantQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public EnchantQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        Enchantments enchantments = plugin.getInventoryManager().getEnchantments();
        String enchantmentName = accessor.getString("enchantment");
        String materialName = accessor.getString("material", null);

        Material material = materialName == null ? null : Material.valueOf(materialName.toUpperCase());
        Enchantment enchantment = enchantmentName == null ? null : enchantments.getEnchantments(enchantmentName).map(MenuEnchantment::enchantment).orElse(null);
        int minimumLevel = accessor.getInt("minimum-level", 0);
        int minimumCost = accessor.getInt("minimum-cost", 0);

        return new EnchantmentAction(material, questType, enchantment, minimumLevel, minimumCost);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.ENCHANT);
    }
}
