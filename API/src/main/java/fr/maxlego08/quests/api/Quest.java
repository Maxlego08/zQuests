package fr.maxlego08.quests.api;

import fr.maxlego08.menu.api.requirement.Action;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
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
     * Gets the type of the quest.
     *
     * @return the type of the quest
     */
    QuestType getType();

    /**
     * Retrieves a specific parameter by its key and type.
     *
     * @param key  the key of the parameter to retrieve
     * @param type the class type of the parameter
     * @param <T>  the type of the parameter
     * @return the parameter value cast to the specified type
     */
    <T> T getParameter(String key, Class<T> type);

    /**
     * Retrieves all parameters associated with the quest.
     *
     * @return a map of parameters
     */
    Map<String, Object> getParameters();

    /**
     * Sets the parameters for the quest.
     *
     * @param section a map containing the parameters to set
     */
    void setParameters(Map<String, Object> section);


    void onComplete(ActiveQuest activeQuest);

}
