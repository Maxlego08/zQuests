package fr.maxlego08.quests.api;

import fr.maxlego08.menu.api.engine.InventoryEngine;

import java.util.Date;
import java.util.UUID;

/**
 * Represents an active quest with various attributes and behaviors.
 */
public interface ActiveQuest {

    /**
     * Gets the unique identifier of the active quest.
     *
     * @return the unique identifier
     */
    UUID getUniqueId();

    /**
     * Gets the quest associated with this active quest.
     *
     * @return the quest
     */
    Quest getQuest();

    /**
     * Gets the current progress limit of the quest.
     *
     * @return the progress limit
     */
    long getAmount();

    /**
     * Sets the current progress limit of the quest.
     *
     * @param amount the limit to set
     */
    void setAmount(long amount);

    /**
     * Adds to the current progress limit of the quest.
     *
     * @param amount the limit to add
     */
    void addAmount(long amount);

    /**
     * Checks if the quest is complete.
     *
     * @return true if the quest is complete, false otherwise
     */
    boolean isComplete();

    /**
     * Checks if the quest is of a specific type.
     *
     * @param type the type to check against
     * @return true if the quest is of the specified type, false otherwise
     */
    boolean isType(QuestType type);

    /**
     * Checks if the quest is owned by a specific unique identifier.
     *
     * @param uniqueId the unique identifier to check
     * @return true if owned by the specified identifier, false otherwise
     */
    boolean owningBy(UUID uniqueId);

    /**
     * Increments the current progress limit of the quest.
     *
     * @param amount the limit to increment
     * @return true if the quest became complete as a result of the increment, false otherwise
     */
    boolean increment(long amount);

    /**
     * This method sets the progress limit directly, instead of incrementing it.
     * For example, for verifying a player's job level.
     *
     * @param amount the limit to set for the progress
     * @return true if the quest became complete as a result of setting the progress, false otherwise
     */
    boolean incrementStatic(long amount);

    /**
     * Completes the quest and returns the completed quest data.
     *
     * @return the completed quest data
     */
    CompletedQuest complete();

    /**
     * Checks if a given object is a quest action.
     *
     * @param object the object to check
     * @return true if it is a quest action, false otherwise
     */
    boolean isQuestAction(Object object);

    /**
     * Checks if the quest is marked as favorite.
     *
     * @return true if the quest is marked as favorite, false otherwise
     */
    boolean isFavorite();

    /**
     * Sets whether the quest is marked as favorite or not.
     *
     * @param favorite true if the quest should be marked as favorite, false otherwise
     */
    void setFavorite(boolean favorite);

    /**
     * Gets the date when this active quest was created.
     *
     * @return the creation date
     */
    Date getCreatedAt();

    /**
     * Checks if the player can complete the quest.
     *
     * @param uuid            the player's unique identifier
     * @param inventoryEngine the inventory engine used to check the player's inventory
     * @return true if the player can complete the quest, false otherwise
     */
    boolean canComplete(UUID uuid, InventoryEngine inventoryEngine);

    long getStartPlayTime();
}