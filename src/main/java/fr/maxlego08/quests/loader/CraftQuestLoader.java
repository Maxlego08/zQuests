package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.ItemStackAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;

import java.io.File;
import java.util.List;

public class CraftQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public CraftQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        if (accessor.contains("material")) {

            Material material = Material.valueOf(accessor.getString("material").toUpperCase());
            int fireworkPower = accessor.getInt("firework-power", 0);

            return new ItemStackAction(material, questType, fireworkPower);
        } else {
            this.plugin.getLogger().severe("Impossible to find the tag or material for " + questType + " in file " + file.getAbsolutePath());
        }

        return null;
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.CRAFT);
    }
}
