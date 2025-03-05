package fr.maxlego08.quests.api;

import java.util.List;
import java.util.Optional;

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
     * Checks if the user has a quest active.
     *
     * @param questName the name of the quest to check
     * @return true if the user has the quest active, false otherwise
     */
    boolean isQuestActive(String questName);

    /**
     * Checks if the user has completed a quest.
     *
     * @param quest the quest to check
     * @return true if the user has completed the quest, false otherwise
     */
    boolean isQuestCompleted(Quest quest);

    /**
     * Checks if the user has completed a quest.
     *
     * @param questName the name of the quest to check
     * @return true if the user has completed the quest, false otherwise
     */
    boolean isQuestCompleted(String questName);

    /**
     * Checks if the user can start a quest.
     *
     * @param quest the quest to check
     * @return true if the user can start the quest, false otherwise
     */
    boolean canStartQuest(Quest quest);

    /**
     * Finds an active quest by its name.
     *
     * @param questName the name of the quest to find
     * @return an Optional containing the active quest if found, otherwise an empty Optional
     */
    Optional<ActiveQuest> findActive(String questName);

    /**
     * Removes an active quest from the user's active quests.
     *
     * @param activeQuest the active quest to remove
     */
    void removeActiveQuest(ActiveQuest activeQuest);
}
