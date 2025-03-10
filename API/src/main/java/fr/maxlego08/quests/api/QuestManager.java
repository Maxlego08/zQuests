package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.event.QuestEvent;
import fr.maxlego08.quests.api.utils.CustomReward;
import fr.maxlego08.quests.api.utils.InventoryContent;
import fr.maxlego08.quests.api.utils.QuestHistory;
import fr.maxlego08.quests.api.utils.QuestInventoryPage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface QuestManager {

    /**
     * Loads all the buttons related to quests.
     */
    void loadButtons();

    /**
     * Load quest patterns from files.
     */
    void loadPatterns();

    /**
     * Load inventories related to quests.
     */
    void loadInventories();

    /**
     * Load all quests from the default location.
     */
    void loadQuests();

    /**
     * Load quests from a specified file.
     *
     * @param file the file to load quests from
     * @return a list of loaded quests
     */
    List<Quest> loadQuests(File file);

    /**
     * Retrieve all available quests.
     *
     * @return a list of quests
     */
    List<Quest> getQuests();

    /**
     * Handle player join event related to quests.
     *
     * @param player the player joining
     */
    void handleJoin(Player player);

    /**
     * Handle player quit event related to quests.
     *
     * @param uuid the unique identifier of the player
     */
    void handleQuit(UUID uuid);

    /**
     * Handle quest actions for a user.
     *
     * @param uuid   the unique identifier of the user
     * @param type   the type of quest
     * @param amount the amount to increment
     * @param object additional data for the quest action
     */
    int handleQuests(UUID uuid, QuestType type, int amount, Object object);

    /**
     * Handle quest actions for a user.
     *
     * @param uuid     the unique identifier of the user
     * @param type     the type of quest
     * @param amount   the amount to increment
     * @param object   additional data for the quest action
     * @param consumer a consumer that will be called with the
     *                 updated active quest, if the quest is complete
     *                 it will be called after the quest has been
     *                 completed
     */
    int handleQuests(UUID uuid, QuestType type, int amount, Object object, Consumer<ActiveQuest> consumer);

    /**
     * Handle static quests (quests that can be completed multiple times)
     * for a user.
     *
     * @param uuid   the unique identifier of the user
     * @param type   the type of quest
     * @param amount the amount to increment
     * @param object additional data for the quest action
     */
    int handleStaticQuests(UUID uuid, QuestType type, int amount, Object object);

    /**
     * Handle inventory quests for the player.
     *
     * @param inventoryContent the player's current inventory content
     * @return the number of active quests that were completed
     */
    int handleInventoryQuests(InventoryContent inventoryContent);

    /**
     * Handle static quests (quests that can be completed multiple times)
     * for a user.
     * <p>
     * This method is similar to {@link #handleQuests(UUID, QuestType, int, Object, Consumer)}
     * but it won't remove the active quest from the player's active quests
     * if it is complete.
     *
     * @param uuid     the unique identifier of the user
     * @param type     the type of quest
     * @param amount   the amount to increment
     * @param object   additional data for the quest action
     * @param consumer a consumer that will be called with the
     *                 updated active quest, if the quest is complete
     *                 it will be called after the quest has been
     *                 completed
     * @return the number of active quests that were completed
     */
    int handleStaticQuests(UUID uuid, QuestType type, int amount, Object object, Consumer<ActiveQuest> consumer);

    /**
     * Add a quest to a player's active quests.
     *
     * @param uuid  the player to add the quest to
     * @param quest the quest to add
     * @param store whether to store the active quest
     * @return an optional containing the added active quest if successful, empty otherwise
     */
    Optional<ActiveQuest> addQuestToPlayer(UUID uuid, Quest quest, boolean store);

    /**
     * Retrieve active quests for a player.
     *
     * @param uuid the unique identifier of the player
     * @return a list of active quests
     */
    List<ActiveQuest> getQuestsFromPlayer(UUID uuid);

    /**
     * Retrieve a quest by name.
     *
     * @param name the name of the quest
     * @return an optional containing the quest if found, empty otherwise
     */
    Optional<Quest> getQuest(String name);

    /**
     * Complete the given active quest.
     *
     * @param activeQuest the active quest to complete
     */
    void completeQuest(ActiveQuest activeQuest);

    /**
     * Retrieve the user's quest data or create a new one if not found.
     *
     * @param uuid the unique identifier of the user
     * @return the user's quest data
     */
    UserQuest getUserQuest(UUID uuid);

    /**
     * Activate a quest for a player.
     *
     * @param sender    the command sender
     * @param player    the player to activate the quest for
     * @param questName the name of the quest to activate
     */
    void activateQuest(CommandSender sender, Player player, String questName);

    /**
     * Complete a quest for a player.
     *
     * @param sender    the command sender
     * @param player    the player to complete the quest for
     * @param questName the name of the quest to complete
     */
    void completeQuest(CommandSender sender, Player player, String questName);

    /**
     * Complete a quest group for a player.
     *
     * @param sender    the command sender
     * @param player    the player to complete the quest group for
     * @param groupName the name of the quest group to complete
     */
    void completeQuestGroup(CommandSender sender, Player player, String groupName);

    /**
     * Delete a quest from a player's active quests.
     *
     * @param sender    the command sender
     * @param player    the player to delete the quest from
     * @param questName the name of the quest to delete
     */
    void deleteUserQuest(CommandSender sender, Player player, String questName);

    /**
     * Delete all quests from a player's active quests.
     *
     * @param sender the command sender
     * @param player the offline player to delete quests from
     */
    void deleteUserQuests(CommandSender sender, Player player);

    /**
     * Set the progress of a quest for a player.
     *
     * @param sender    the command sender
     * @param player    the player to set the progress for
     * @param questName the name of the quest to set the progress for
     * @param amount    the amount to set the progress to
     */
    void setQuestProgress(CommandSender sender, Player player, String questName, int amount);

    /**
     * Add a number to the progress of a quest for a player.
     *
     * @param sender    the command sender
     * @param player    the player to add progress to
     * @param questName the name of the quest to add progress to
     * @param amount    the amount to add to the progress
     */
    void addQuestProgress(CommandSender sender, Player player, String questName, int amount);

    /**
     * Open the quest inventory for a player.
     *
     * @param player             the player to open the quest inventory for
     * @param questInventoryPage the page to open
     */
    void openQuestInventory(Player player, QuestInventoryPage questInventoryPage);


    /**
     * Get all the custom rewards registered in the plugin.
     *
     * @return a list of all the custom rewards
     */
    List<CustomReward> getCustomRewards();

    /**
     * Retrieves a quest group by its name.
     *
     * @param key the name of the quest group to retrieve
     * @return an Optional containing the quest group if found, otherwise an empty Optional
     */
    Optional<QuestsGroup> getGroups(String key);

    /**
     * Retrieve all quest groups.
     *
     * @return a map where the keys are group names and the values are the corresponding quest groups
     */
    Map<String, QuestsGroup> getGroups();

    /**
     * Starts all the given quests for the player with the given uuid.
     *
     * @param uuid   the uuid of the player to start the quests for
     * @param quests the list of quests to start
     */
    void startQuests(UUID uuid, List<Quest> quests);

    /**
     * Set a quest as favorite for a player.
     *
     * @param sender    the command sender
     * @param player    the player to set the favorite for
     * @param questName the name of the quest to set as favorite
     * @param amount    true if the quest should be marked as favorite, false otherwise
     */
    void setFavorite(CommandSender sender, Player player, String questName, boolean amount);

    /**
     * Call a quest event. This method will call the event and return
     * whether or not the event was cancelled.
     *
     * @param playerUniqueId the uuid of the player that the event is for
     * @param event          the event to call
     * @return true if the event was cancelled, false otherwise
     */
    boolean callQuestEvent(UUID playerUniqueId, QuestEvent event);

    /**
     * Restarts a quest for a player.
     *
     * @param sender        the command sender
     * @param offlinePlayer the player to restart the quest for
     * @param questName     the name of the quest to restart
     */
    void restartUserQuest(CommandSender sender, OfflinePlayer offlinePlayer, String questName);

    /**
     * Retrieves the quest group of the given quest.
     *
     * @param quest the quest to get the group of
     * @return an Optional containing the quest group if found, otherwise an empty Optional
     */
    List<QuestsGroup> getGroups(Quest quest);

    /**
     * Retrieves the quest group that the given quest is part of.
     *
     * @param quest the quest to get the group of
     * @return an Optional containing the quest group if found, otherwise an empty Optional
     */
    Optional<QuestsGroup> getGroup(Quest quest);

    /**
     * Retrieves a list of quests to display for the given player.
     *
     * @param player the player for whom the quests will be displayed
     * @return a list of QuestHistory objects representing the quests to display
     */
    List<QuestHistory> getDisplayQuests(Player player);
}
