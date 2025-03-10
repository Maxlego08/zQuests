package fr.maxlego08.quests.api;

import java.util.List;

public interface QuestsGroup {

    /**
     * Retrieves the unique name of the quest group.
     *
     * @return the name of the quest group
     */
    String getName();

    /**
     * Gets the display name of the quest group.
     *
     * @return the display name of the quest group
     */
    String getDisplayName();

    /**
     * Gets the list of quests associated with this quest group.
     *
     * @return the list of quests associated with this quest group
     */
    List<Quest> getQuests();

    /**
     * Gets the list of names of the quests associated with this quest group.
     *
     * @return the list of names of the quests associated with this quest group
     */
    List<String> getQuestNames();

    /**
     * Sets the list of quests associated with this quest group.
     *
     * @param quests the list of quests associated with this quest group
     */
    void setQuests(List<Quest> quests);

    /**
     * Determines if a quest needs to be marked as a favorite for a user.
     *
     * @param quest the quest to check
     * @param userQuest the user's quest data
     * @return true if the quest needs to be marked as a favorite, false otherwise
     */
    boolean needFavorite(Quest quest, UserQuest userQuest);

    /**
     * Retrieves the default custom model ID for the quest group.
     *
     * @return the default custom model ID for the quest group
     */
    int getDefaultCustomModelId();
}
