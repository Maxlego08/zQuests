package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public abstract class ActionInfo<T> {

    private final QuestType questType;
    private final T value;

    public ActionInfo(QuestType questType, T value) {
        this.questType = questType;
        this.value = value;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public T getValue() {
        return value;
    }
}
