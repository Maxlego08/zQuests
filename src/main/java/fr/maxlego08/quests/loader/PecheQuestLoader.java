package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.PecheAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;

import java.io.File;
import java.util.List;

public class PecheQuestLoader implements QuestActionLoader {

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType type, File file) {
        return new PecheAction(type, accessor.getString("poisson", "*"), accessor.getString("rarete", "*"),
                accessor.getInt("palier", 0));
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.CUSTOM_FISHING, QuestType.SELL_FISH);
    }
}
