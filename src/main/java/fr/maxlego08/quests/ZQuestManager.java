package fr.maxlego08.quests;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.inventories.loader.StartQuestLoader;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ZQuestManager extends ZUtils implements QuestManager {

    private final QuestsPlugin plugin;
    private final List<Quest> quests = new ArrayList<>();
    private final Map<UUID, UserQuest> usersQuests = new HashMap<>();

    public ZQuestManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadButtons() {
        var buttonManager = this.plugin.getButtonManager();
        buttonManager.registerAction(new StartQuestLoader(this.plugin));

    }

    @Override
    public void loadPatterns() {
        var patternManager = this.plugin.getPatternManager();

        File folder = new File(this.plugin.getDataFolder(), "patterns");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        files(folder, file -> {
            try {
                patternManager.loadPattern(file);
            } catch (InventoryException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void loadInventories() {
        var inventoryManager = this.plugin.getInventoryManager();
        inventoryManager.deleteInventories(this.plugin);

        File folder = new File(this.plugin.getDataFolder(), "inventories");
        if (!folder.exists()) {
            folder.mkdirs();
            this.plugin.saveResource("inventories/quests.yml", false);
        }

        files(folder, file -> {
            try {
                inventoryManager.loadInventory(plugin, file);
            } catch (InventoryException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void loadQuests() {

        File folder = new File(this.plugin.getDataFolder(), "quests");
        if (!folder.exists()) {
            folder.mkdirs();
            this.plugin.saveResource("quests/example_blocks.yml", false);
            this.plugin.saveResource("quests/example_entities.yml", false);
            this.plugin.saveResource("quests/example_farming.yml", false);
            this.plugin.saveResource("quests/example_smelt.yml", false);
            this.plugin.saveResource("quests/example_fish.yml", false);
            this.plugin.saveResource("quests/example_enchant.yml", false);
            this.plugin.saveResource("quests/example_brew.yml", false);
            this.plugin.saveResource("quests/example_craft.yml", false);
        }

        this.files(folder, file -> this.quests.addAll(this.loadQuests(file)));

        // Check quests names

        Set<String> questNames = new HashSet<>();
        Iterator<Quest> iterator = this.quests.iterator();
        while (iterator.hasNext()) {
            Quest quest = iterator.next();
            if (!questNames.add(quest.getName())) {
                this.plugin.getLogger().warning("A quest with the name " + quest.getName() + " already exists");
                iterator.remove();
            }
        }

        this.plugin.getLogger().info(this.quests.size() + " quests loaded");
    }

    @Override
    public List<Quest> loadQuests(File file) {

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        QuestLoader questLoader = new QuestLoader(this.plugin);
        return configuration.getMapList("quests").stream().map(map -> new TypedMapAccessor((Map<String, Object>) map)).map(typedMapAccessor -> questLoader.loadQuest(typedMapAccessor, file)).filter(Objects::nonNull).toList();
    }

    @Override
    public List<Quest> getQuests() {
        return quests;
    }

    @Override
    public void handleJoin(Player player) {
        this.plugin.getStorageManager().load(player.getUniqueId(), userQuest -> {

            this.usersQuests.put(player.getUniqueId(), userQuest);

            this.quests.stream().filter(Quest::isAutoAccept).filter(quest -> {
                return userQuest.getActiveQuests().stream().noneMatch(activeQuest -> activeQuest.getQuest().equals(quest));
            }).forEach(quest -> this.addQuestToPlayer(player, quest));
        });
    }

    @Override
    public void handleQuit(UUID uuid) {
        UserQuest userQuest = this.usersQuests.remove(uuid);
        if (userQuest == null) return;
        userQuest.getActiveQuests().forEach(activeQuest -> this.plugin.getStorageManager().upsert(activeQuest));
    }

    @Override
    public void handleQuests(UUID uuid, QuestType type, int amount, Object object) {
        // Retrieve the user's quest data or create a new ZUserQuest if not found
        var userQuest = getUserQuest(uuid);

        // Stream through the active quests of the user
        var iterator = userQuest.getActiveQuests().iterator();
        while (iterator.hasNext()) {
            ActiveQuest activeQuest = iterator.next();
            if (activeQuest.getQuest().getType() == type && !activeQuest.isComplete() && activeQuest.isQuestAction(object)) {
                if (activeQuest.increment(amount)) { // Increment the progress of the quest
                    iterator.remove(); // If the quest is complete, remove it from the list
                    this.completeQuest(activeQuest);
                }
                this.plugin.getStorageManager().softUpsert(activeQuest); // Soft update the quest in storage
            }
        }
    }

    @Override
    public void addQuestToPlayer(Player player, Quest quest) {
        // Create a new active quest for the player
        ActiveQuest activeQuest = new ZActiveQuest(player.getUniqueId(), quest, 0);
        var userQuest = getUserQuest(player.getUniqueId());

        // Check if the user already completes the quest
        if (userQuest.getCompletedQuests().stream().anyMatch(completedQuest -> completedQuest.quest().equals(quest))) {
            return; // Exit if the quest is already completed
        }

        // Add the active quest to the user's active quests
        userQuest.getActiveQuests().add(activeQuest);

        // Persist the new active quest in storage
        this.plugin.getStorageManager().upsert(activeQuest);
    }

    @Override
    public List<ActiveQuest> getQuestsFromPlayer(UUID uuid) {
        return getUserQuest(uuid).getActiveQuests();
    }

    @Override
    public Optional<Quest> getQuest(String name) {
        return this.quests.stream().filter(quest -> quest.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public void completeQuest(ActiveQuest activeQuest) {

        var userQuest = getUserQuest(activeQuest.getUniqueId());

        CompletedQuest completedQuest = activeQuest.complete();
        userQuest.getCompletedQuests().add(completedQuest);
        this.plugin.getStorageManager().upsert(activeQuest.getUniqueId(), completedQuest);
        this.plugin.getStorageManager().delete(activeQuest);
    }

    @Override
    public UserQuest getUserQuest(UUID uuid) {
        return this.usersQuests.computeIfAbsent(uuid, u -> new ZUserQuest());
    }

    @Override
    public void activateQuest(CommandSender sender, Player player, String questName) {

        Optional<Quest> optional = this.getQuest(questName);
        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        Quest quest = optional.get();
        UserQuest userQuest = getUserQuest(player.getUniqueId());
        if (userQuest.canStartQuest(quest)) {
            this.addQuestToPlayer(player, quest);
            message(sender, Message.QUEST_START_SUCCESS, "%name%", questName, "%player%", player.getName());
        } else {
            message(sender, Message.QUEST_START_ERROR, "%name%", questName, "%player%", player.getName());
        }
    }


    @Override
    public void completeQuest(CommandSender sender, Player player, String questName) {
        var userQuest = getUserQuest(player.getUniqueId());
        var optional = userQuest.getActiveQuests().stream().filter(a -> a.getQuest().getName().equalsIgnoreCase(questName)).findFirst();

        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        userQuest.getActiveQuests().remove(optional.get());
        this.completeQuest(optional.get());

        message(sender, Message.QUEST_COMPLETE_SUCCESS, "%name%", questName, "%player%", player.getName());
    }

    @Override
    public void deleteUserQuest(CommandSender sender, Player player, String questName) {

        var userQuest = getUserQuest(player.getUniqueId());

        Optional<Quest> optional = this.getQuest(questName);
        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        var quest = optional.get();

        userQuest.getActiveQuests().removeIf(activeQuest -> activeQuest.getQuest() == quest);
        userQuest.getCompletedQuests().removeIf(completedQuest -> completedQuest.quest() == quest);

        this.plugin.getStorageManager().deleteQuest(player.getUniqueId(), quest.getName());

        message(sender, Message.QUEST_DELETE_SUCCESS, "%name%", questName, "%player%", player.getName());
    }

    @Override
    public void deleteUserQuests(CommandSender sender, OfflinePlayer offlinePlayer) {
        var userQuest = getUserQuest(offlinePlayer.getUniqueId());

        plugin.getStorageManager().deleteAll(offlinePlayer.getUniqueId());
        userQuest.getActiveQuests().clear();
        userQuest.getCompletedQuests().clear();

        message(sender, Message.QUEST_DELETE_ALL_SUCCESS, "%player%", offlinePlayer.getName());
    }

    @Override
    public void setQuestProgress(CommandSender sender, Player player, String questName, int amount) {
        var userQuest = getUserQuest(player.getUniqueId());
        var optional = userQuest.getActiveQuests().stream().filter(a -> a.getQuest().getName().equalsIgnoreCase(questName)).findFirst();

        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        ActiveQuest activeQuest = optional.get();
        activeQuest.setAmount(amount);
        this.plugin.getStorageManager().upsert(activeQuest);

        message(sender, Message.QUEST_SET_PROGRESS_SUCCESS, "%name%", questName, "%player%", player.getName(), "%progress%", amount);
    }

    @Override
    public void addQuestProgress(CommandSender sender, Player player, String questName, int amount) {
        var userQuest = getUserQuest(player.getUniqueId());
        var optional = userQuest.getActiveQuests().stream().filter(a -> a.getQuest().getName().equalsIgnoreCase(questName)).findFirst();

        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        ActiveQuest activeQuest = optional.get();
        activeQuest.addAmount(amount);
        this.plugin.getStorageManager().upsert(activeQuest);

        message(sender, Message.QUEST_ADD_PROGRESS_SUCCESS, "%name%", questName, "%player%", player.getName(), "%progress%", amount);
    }

    @Override
    public void openQuestInventory(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "quests");
    }
}
