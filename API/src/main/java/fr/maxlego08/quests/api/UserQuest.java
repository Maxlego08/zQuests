package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.hologram.QuestHologram;

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
     * Gets the list of active quests for the user sorted by the sortActiveQuests method.
     *
     * @return the list of active quests sorted by the sortActiveQuests method
     */
    List<ActiveQuest> getSortActiveQuests();

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
     * Finds an active quest by its quest object.
     *
     * @param quest the quest object to find
     * @return an Optional containing the active quest if found, otherwise an empty Optional
     */
    Optional<ActiveQuest> findActive(Quest quest);

    /**
     * Removes an active quest from the user's active quests.
     *
     * @param activeQuest the active quest to remove
     */
    void removeActiveQuest(ActiveQuest activeQuest);

    /**
     * Retrieves the list of active quests that are marked as favorite.
     *
     * @return a list of favorite active quests
     */
    List<ActiveQuest> getFavoriteQuests();

    /**
     * Finds a completed quest by its name.
     *
     * @param questName the name of the quest to find
     * @return an Optional containing the completed quest if found, otherwise an empty Optional
     */
    Optional<CompletedQuest> findComplete(String questName);

    /**
     * Finds a completed quest by its quest object.
     *
     * @param quest the quest object to find
     * @return an Optional containing the completed quest if found, otherwise an empty Optional
     */
    Optional<CompletedQuest> findComplete(Quest quest);

    /**
     * Retrieves the current quest group for the user.
     *
     * @return the name of the current quest group
     */
    String getCurrentGroup();

    /**
     * Sets the current quest group for the user.
     *
     * @param group the name of the new current quest group
     */
    void setCurrentGroup(String group);

    boolean isExtend();

    void setExtend(boolean extend);

    List<QuestHologram> getHolograms();

    void addHologram(QuestHologram questHologram);

    void removeHologram(QuestHologram questHologram);
}
