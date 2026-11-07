package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.OneBlockProgressAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import java.io.File;
import java.util.List;

public class OneBlockProgressQuestLoader implements QuestActionLoader {
    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType type, File file) {
        return new OneBlockProgressAction(type, type == QuestType.ONEBLOCK_PHASE ? accessor.getString("phase", "*") : "*");
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.ONEBLOCK_LEVEL, QuestType.ONEBLOCK_PRESTIGE, QuestType.ONEBLOCK_PHASE);
    }
}
