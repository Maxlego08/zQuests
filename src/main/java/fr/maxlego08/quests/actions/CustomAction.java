package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

public class CustomAction implements QuestAction {

    private final String data;

    public CustomAction(String data) {
        this.data = data;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof String string && string.equals(this.data);
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.CUSTOM;
    }
}
