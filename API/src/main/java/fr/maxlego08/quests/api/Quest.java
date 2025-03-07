package fr.maxlego08.quests.api;

import fr.maxlego08.menu.api.requirement.Action;
import org.bukkit.Material;

import java.util.List;

/**
 * Represents a quest with various attributes and behaviors.
 */
public interface Quest {

    /**
     * Gets the unique name of the quest.
     *
     * @return the name of the quest
     */
    String getName();

    /**
     * Gets the display name of the quest.
     *
     * @return the display name of the quest
     */
    String getDisplayName();

    /**
     * Gets the description of the quest.
     *
     * @return the description of the quest
     */
    String getDescription();

    /**
     * Gets the thumbnail material of the quest.
     *
     * @return the thumbnail material of the quest
     */
    Material getThumbnail();

    /**
     * Gets the goal amount for the quest.
     *
     * @return the goal amount of the quest
     */
    long getGoal();

    /**
     * Checks if the quest is set to auto-accept.
     *
     * @return true if the quest is auto-accept, false otherwise
     */
    boolean isAutoAccept();

    /**
     * Gets the list of rewards associated with the quest.
     *
     * @return the list of rewards
     */
    List<Action> getRewards();

    /**
     * Gets the type of the quest.
     *
     * @return the type of the quest
     */
    QuestType getType();

    /**
     * Gets the list of actions associated with the quest.
     *
     * @return the list of actions
     */
    List<QuestAction> getActions();

    /**
     * Called when the quest is completed.
     *
     * @param activeQuest the active quest that was completed
     */
    void onComplete(ActiveQuest activeQuest);

    /**
     * Checks if the quest uses global rewards.
     *
     * @return true if the quest uses global rewards, false otherwise
     */
    boolean useGlobalRewards();

    /**
     * Checks if the quest can be marked or unmarked as favorite.
     *
     * @return true if the quest can be marked or unmarked as favorite, false otherwise
     */
    boolean canChangeFavorite();

    /**
     * Checks if the quest is marked as favorite.
     *
     * @return true if the quest is marked as favorite, false otherwise
     */
    boolean isFavorite();

}
