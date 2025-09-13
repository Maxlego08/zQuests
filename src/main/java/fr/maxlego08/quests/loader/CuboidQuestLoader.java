package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.CuboidAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.zcore.utils.ZUtils;

import java.io.File;
import java.util.List;

public class CuboidQuestLoader extends ZUtils implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public CuboidQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        String stringCuboid = accessor.getString("cuboid", null);
        if (stringCuboid != null) {

            var cuboid = changeStringToCuboid(stringCuboid);
            if (cuboid != null) {
                return new CuboidAction(cuboid);
            } else {
                this.plugin.getLogger().severe("Impossible to find the cuboid for " + questType + " in file " + file.getAbsolutePath());
            }
        } else {
            this.plugin.getLogger().severe("Impossible to find the cuboid for " + questType + " in file " + file.getAbsolutePath());
        }
        return null;
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.CUBOID, QuestType.LOOK_AT_BLOCK, QuestType.LOOK_AT_ENTITY);
    }
}
