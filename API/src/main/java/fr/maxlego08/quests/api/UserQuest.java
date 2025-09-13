package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.hologram.QuestHologram;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.quests.api.waypoint.QuestWayPoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    /**
     * Returns whether the user is currently in an extended state.
     * This extended state is used to display quest group on inventories.
     *
     * @return whether the user is in an extended state
     */
    boolean isExtend();

    /**
     * Sets the user's extended state.
     *
     * @param extend true to enable the extended state, false to disable it
     */
    void setExtend(boolean extend);

    /**
     * Retrieves a list of all quest holograms associated with the user.
     *
     * @return a list of quest holograms
     */
    List<QuestHologram> getHolograms();

    /**
     * Adds a quest hologram to the user's list of holograms.
     *
     * @param questHologram The quest hologram to be added.
     */
    void addHologram(QuestHologram questHologram);

    /**
     * Removes a quest hologram from the user's list of holograms.
     *
     * @param questHologram The quest hologram to be removed.
     */
    void removeHologram(QuestHologram questHologram);

    /**
     * Retrieves the unique identifier associated with the user.
     *
     * @return the UUID of the user
     */
    UUID getUniqueId();

    /**
     * Retrieves the hologram associated with the specified quest.
     *
     * @param quest The quest whose hologram is to be retrieved.
     * @return An Optional containing the quest hologram if present, otherwise an empty Optional.
     */
    Optional<QuestHologram> getHologram(Quest quest);

    /**
     * Checks if the user has marked a quest as favorite.
     *
     * @param questId the id of the quest to check
     * @return true if the user has marked the quest as favorite, false otherwise
     */
    boolean isFavorite(String questId);

    /**
     * Retrieves the maximum number of favorite quests that the user can have.
     *
     * @return the maximum number of favorite quests
     */
    int getFavoriteLimit();

    /**
     * Sets the maximum number of favorite quests that the user can have.
     *
     * @param limit the new maximum number of favorite quests
     */
    void setFavoriteLimit(int limit);

    /**
     * Retrieves the current favorite placeholder type for the user.
     *
     * @return the favorite placeholder type
     */
    FavoritePlaceholderType getFavoritePlaceholderType();

    /**
     * Sets the favorite placeholder type for the user.
     *
     * @param favoritePlaceholderType the new favorite placeholder type
     */
    void setFavoritePlaceholderType(FavoritePlaceholderType favoritePlaceholderType);

    /**
     * Retrieves a list of all quest waypoints associated with the user.
     *
     * @return a list of quest waypoints
     */
    List<QuestWayPoint> getQuestWayPoints();

    /**
     * Adds a quest waypoint to the user's collection of waypoints.
     *
     * @param questWayPoint the quest waypoint to add
     */
    void addWayPoint(QuestWayPoint questWayPoint);

    /**
     * Removes a quest waypoint from the user's collection of waypoints.
     *
     * @param questWayPoint the quest waypoint to remove
     */
    void removeWayPoint(QuestWayPoint questWayPoint);

    /**
     * Retrieves the quest waypoint associated with the user and the specified quest.
     *
     * @param quest the quest whose waypoint is to be retrieved
     * @return an Optional containing the quest waypoint if present, otherwise an empty Optional
     */
    Optional<QuestWayPoint> getWayPoint(Quest quest);

    void deleteHolograms();

    void deleteWayPoints();
}
