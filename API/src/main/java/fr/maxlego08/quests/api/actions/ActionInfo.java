package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public abstract class ActionInfo<T> {

    private final QuestType questType;
    private final T value;

    public ActionInfo(QuestType questType, T value) {
        this.questType = questType;
        this.value = value;
    }

    /**
     * Gets the type of quest associated with this action.
     *
     * @return The type of quest.
     */
    public QuestType getQuestType() {
        return questType;
    }

    /**
     * Gets the value associated with the action.
     *
     * @return The value associated with the action.
     */
    public T getValue() {
        return value;
    }
}
