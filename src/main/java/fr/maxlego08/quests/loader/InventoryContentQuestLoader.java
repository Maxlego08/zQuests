package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.InventoryContentAction;
import fr.maxlego08.quests.actions.InventoryOpenAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.zcore.utils.TagRegistry;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.io.File;
import java.util.List;

public class InventoryContentQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public InventoryContentQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        String citizenName = accessor.contains("citizen-name") ? accessor.getString("citizen-name") : null;
        Material material = accessor.contains("material") ? Material.valueOf(accessor.getString("material").toUpperCase()) : null;
        Tag<Material> materialTag = accessor.contains("tag") ? TagRegistry.getTag(accessor.getString("tag").toUpperCase()) : null;

        if (material == null && materialTag == null) {
            this.plugin.getLogger().severe("Impossible to find the material or tag for " + questType + " in file " + file.getAbsolutePath());
            return null;
        }

        int customModelId = accessor.getInt("custom-model-id", 0);

        return new InventoryContentAction(citizenName, material, materialTag, customModelId);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.INVENTORY_CONTENT);
    }
}
