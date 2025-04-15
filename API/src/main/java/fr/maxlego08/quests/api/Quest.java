package fr.maxlego08.quests.api;

import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.quests.api.hologram.HologramConfiguration;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

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
     * Gets the list of start actions associated with the quest.
     *
     * @return the list of start actions
     */
    List<Action> getStartActions();

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

    /**
     * Retrieves the custom model ID associated with the quest.
     *
     * @return the custom model ID
     */
    int getCustomModelId();


    /**
     * Checks if the quest can be activated only once. For example, when talking to an NPC,
     * this quest will interrupt the activation of other quests.
     *
     * @return true if the quest can be activated only once, false otherwise
     */
    boolean isUnique();

    /**
     * Checks if the quest is hidden.
     *
     * @return true if the quest is hidden, false otherwise
     */
    boolean isHidden();

    /**
     * Retrieves the hologram configuration associated with the quest.
     *
     * @return the hologram configuration
     */
    HologramConfiguration getHologramConfiguration();

    /**
     * Checks if the quest has a hologram associated with it.
     *
     * @return true if the quest has a hologram, false otherwise
     */
    boolean hasHologram();

    String getHologramName(UUID uuid);

}
