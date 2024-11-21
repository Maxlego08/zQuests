package fr.maxlego08.quests.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestManager {

    /**
     * Load button actions for quests.
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
    void handleQuests(UUID uuid, QuestType type, int amount, Object object);

    /**
     * Add a quest to a player's active quests.
     *
     * @param player the player to add the quest to
     * @param quest  the quest to add
     */
    void addQuestToPlayer(Player player, Quest quest);

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
     * @param sender        the command sender
     * @param offlinePlayer the offline player to delete quests from
     */
    void deleteUserQuests(CommandSender sender, OfflinePlayer offlinePlayer);

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
     * @param player the player to open the quest inventory for
     */
    void openQuestInventory(Player player);
}
