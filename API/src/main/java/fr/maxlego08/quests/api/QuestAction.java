package fr.maxlego08.quests.api;

/**
 * Represents a quest action.
 */
public interface QuestAction {

    /**
     * Check if the given target is an action.
     *
     * @param target the target to check
     * @return true if it is an action, false otherwise
     */
    boolean isAction(Object target);

    /**
     * Get the type of the quest.
     *
     * @return the type of the quest
     */
    QuestType getQuestType();

}
