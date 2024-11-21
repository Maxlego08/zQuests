package fr.maxlego08.quests.api;

import java.util.List;

/**
 * Represents the user's quest data.
 */
public interface UserQuest {

    /**
     * Gets the list of active quests for the user.
     *
     * @return the list of active quests
     */
    List<ActiveQuest> getActiveQuests();

    /**
     * Gets the list of completed quests for the user.
     *
     * @return the list of completed quests
     */
    List<CompletedQuest> getCompletedQuests();

    /**
     * Checks if the user has a quest active.
     *
     * @param quest the quest to check
     * @return true if the user has the quest active, false otherwise
     */
    boolean isQuestActive(Quest quest);

    /**
     * Checks if the user has completed a quest.
     *
     * @param quest the quest to check
     * @return true if the user has completed the quest, false otherwise
     */
    boolean isQuestCompleted(Quest quest);

    /**
     * Checks if the user can start a quest.
     *
     * @param quest the quest to check
     * @return true if the user can start the quest, false otherwise
     */
    boolean canStartQuest(Quest quest);
}
