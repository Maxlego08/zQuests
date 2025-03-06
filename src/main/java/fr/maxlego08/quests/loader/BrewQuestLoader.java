package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.enchantment.Enchantments;
import fr.maxlego08.menu.api.enchantment.MenuEnchantment;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.BrewAction;
import fr.maxlego08.quests.actions.EnchantmentAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.List;

public class BrewQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public BrewQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        String potionName = accessor.getString("potion-type", null);
        String potionMaterialName = accessor.getString("potion-material", "POTION");
        String ingredientName = accessor.getString("ingredient", null);

        PotionType potionType = potionName == null ? null : PotionType.valueOf(potionName.toUpperCase());
        Material material = ingredientName == null ? null : Material.valueOf(ingredientName.toUpperCase());
        Material potionMaterial = Material.valueOf(potionMaterialName.toUpperCase());

        return new BrewAction(potionType, questType, potionMaterial, material);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.BREW);
    }
}
