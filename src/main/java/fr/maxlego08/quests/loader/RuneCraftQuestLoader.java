package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.RuneCraftAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import java.io.File;
import java.util.List;

public class RuneCraftQuestLoader implements QuestActionLoader {
    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType type, File file) {
        String rune = accessor.getString("rune", "*");
        return type == QuestType.RUNE_FORGE
                ? new fr.maxlego08.quests.actions.RuneForgeAction(rune) : new RuneCraftAction(rune);
    }

    @Override
    public List<QuestType> getSupportedTypes() { return List.of(QuestType.RUNE_CRAFT, QuestType.RUNE_FORGE); }
}
