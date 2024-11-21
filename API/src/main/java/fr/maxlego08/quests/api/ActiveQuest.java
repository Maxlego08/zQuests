package fr.maxlego08.quests.api;

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
     * Gets the current progress amount of the quest.
     *
     * @return the progress amount
     */
    long getAmount();

    /**
     * Adds to the current progress amount of the quest.
     *
     * @param amount the amount to add
     */
    void addAmount(long amount);

    /**
     * Sets the current progress amount of the quest.
     *
     * @param amount the amount to set
     */
    void setAmount(long amount);

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
     * Increments the current progress amount of the quest.
     *
     * @param amount the amount to increment
     * @return true if the quest became complete as a result of the increment, false otherwise
     */
    boolean increment(int amount);

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
}