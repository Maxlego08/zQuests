package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

public class ResurrectAction implements QuestAction {
    @Override
    public boolean isAction(Object target) {
        return true;
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.RESURRECT;
    }
}
