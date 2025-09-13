package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.MaterialAction;
import fr.maxlego08.quests.actions.TagAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.zcore.utils.TagRegistry;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MaterialQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public MaterialQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        if (accessor.contains("material")) {

            Material material = Material.valueOf(accessor.getString("material").toUpperCase());
            return new MaterialAction(material, questType);

        } else if (accessor.contains("tag")) {

            Tag<Material> tag = TagRegistry.getTag(accessor.getString("tag").toUpperCase());
            if (tag != null) {
                return new TagAction(tag, questType);
            }
            this.plugin.getLogger().severe("Impossible to find the tag or material for " + questType + " in file " + file.getAbsolutePath());

        } else {

            this.plugin.getLogger().severe("Impossible to find the tag or material for " + questType + " in file " + file.getAbsolutePath());
        }

        return null;
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return Arrays.stream(QuestType.values()).filter(QuestType::isMaterial).toList();
    }
}
