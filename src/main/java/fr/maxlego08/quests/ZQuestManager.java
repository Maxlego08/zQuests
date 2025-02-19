package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.QuestsGroup;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.events.QuestStartEvent;
import fr.maxlego08.quests.api.utils.CustomReward;
import fr.maxlego08.quests.inventories.loader.QuestCompleteLoader;
import fr.maxlego08.quests.inventories.loader.StartQuestLoader;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
    private final List<CustomReward> customRewards = new ArrayList<>();
    private final Map<String, QuestsGroup> groups = new HashMap<>();
    private List<Action> globalRewards = new ArrayList<>();

    public ZQuestManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    private static void getaVoid(Action action, Player player, InventoryDefault fakeInventory) {
        action.preExecute(player, null, fakeInventory, new Placeholders());
    }

    @Override
    public void loadButtons() {
        var buttonManager = this.plugin.getButtonManager();
        buttonManager.registerAction(new StartQuestLoader(this.plugin));
        buttonManager.register(new QuestCompleteLoader(this.plugin));
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

        this.quests.clear();
        this.customRewards.clear();

        this.files(folder, file -> this.quests.addAll(this.loadQuests(file)));

        // Check quests names

        Set<String> questNames = new HashSet<>();
        Iterator<Quest> iterator = this.quests.iterator();
        while (iterator.hasNext()) {
            Quest quest = iterator.next();
            if (!questNames.add(quest.getName())) {
                this.plugin.getLogger().severe("A quest with the name " + quest.getName() + " already exists");
                iterator.remove();
            }
        }

        this.plugin.getLogger().info(this.quests.size() + " quests loaded");
        this.updateOnlyPlayers();
        this.loadGlobalRewards();

        this.loadCustomRewards(this.plugin.getConfig(), new File(this.plugin.getDataFolder(), "config.yml"));
        this.plugin.getLogger().info(this.customRewards.size() + " custom rewards loaded");

        this.loadGroups();
    }

    private void loadGroups() {

        this.groups.clear();

        var config = this.plugin.getInventoryManager().loadYamlConfiguration(new File(this.plugin.getDataFolder(), "config.yml"));
        var section = config.getConfigurationSection("quests-groups");
        if (section == null) return;

        for (String key : section.getKeys(false)) {

            var currentSection = section.getConfigurationSection(key);

            if (currentSection == null) continue;

            String displayName = currentSection.getString("display-name", key);
            List<Quest> quests = currentSection.getStringList("quests").stream().map(this::getQuest).filter(Optional::isPresent).map(Optional::get).toList();

            this.groups.put(key, new ZQuestsGroup(key, displayName, quests));
        }
    }

    private void updateOnlyPlayers() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            UserQuest userQuest = this.getUserQuest(player.getUniqueId());
            List<ActiveQuest> activeQuests = userQuest.getActiveQuests();

            for (int i = 0; i < activeQuests.size(); i++) {
                ActiveQuest activeQuest = activeQuests.get(i);
                Optional<Quest> optionalQuest = this.getQuest(activeQuest.getQuest().getName());
                if (optionalQuest.isPresent()) {
                    Quest quest = optionalQuest.get();
                    activeQuests.set(i, new ZActiveQuest(activeQuest.getUniqueId(), quest, activeQuest.getAmount()));
                }
            }

            List<CompletedQuest> completedQuests = userQuest.getCompletedQuests();
            for (int i = 0; i < completedQuests.size(); i++) {
                CompletedQuest completedQuest = completedQuests.get(i);
                Optional<Quest> optionalQuest = this.getQuest(completedQuest.quest().getName());
                if (optionalQuest.isPresent()) {
                    Quest quest = optionalQuest.get();
                    completedQuests.set(i, new CompletedQuest(quest, completedQuest.completedAt()));
                }
            }
        }
    }

    private void loadGlobalRewards() {
        FileConfiguration configuration = this.plugin.getConfig();
        this.globalRewards = this.plugin.getButtonManager().loadActions((List<Map<String, Object>>) configuration.getList("global-rewards"), "global-rewards", new File(plugin.getDataFolder(), "config.yml"));
    }

    private void loadCustomRewards(FileConfiguration configuration, File file) {
        for (Map<?, ?> map : configuration.getMapList("custom-rewards")) {
            TypedMapAccessor typedMapAccessor = new TypedMapAccessor((Map<String, Object>) map);
            List<String> quests = typedMapAccessor.getStringList("quests");
            List<Action> actions = this.plugin.getButtonManager().loadActions((List<Map<String, Object>>) typedMapAccessor.getList("actions"), "custom-rewards", file);
            this.customRewards.add(new CustomReward(quests, actions));
        }
    }

    @Override
    public List<Quest> loadQuests(File file) {

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        this.loadCustomRewards(configuration, file);

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

            handleDefaultQuest(player.getUniqueId());
        });
    }

    private void handleDefaultQuest(UUID uuid) {
        UserQuest userQuest = getUserQuest(uuid);
        this.quests.stream().filter(Quest::isAutoAccept).filter(quest -> userQuest.getActiveQuests().stream().noneMatch(activeQuest -> activeQuest.getQuest().equals(quest))).forEach(quest -> this.addQuestToPlayer(uuid, quest));
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
    public void handleStaticQuests(UUID uuid, QuestType type, int amount, Object object) {
        // Retrieve the user's quest data or create a new ZUserQuest if not found
        var userQuest = getUserQuest(uuid);

        // Stream through the active quests of the user
        var iterator = userQuest.getActiveQuests().iterator();
        while (iterator.hasNext()) {
            ActiveQuest activeQuest = iterator.next();
            if (activeQuest.getQuest().getType() == type && !activeQuest.isComplete() && activeQuest.isQuestAction(object)) {
                if (activeQuest.incrementStatic(amount)) { // Increment the progress of the quest
                    iterator.remove(); // If the quest is complete, remove it from the list
                    this.completeQuest(activeQuest);
                }
                this.plugin.getStorageManager().softUpsert(activeQuest); // Soft update the quest in storage
            }
        }
    }

    @Override
    public void addQuestToPlayer(UUID uuid, Quest quest) {
        // Create a new active quest for the player
        ActiveQuest activeQuest = new ZActiveQuest(uuid, quest, 0);
        var userQuest = getUserQuest(uuid);

        // Check if the user already completes the quest
        if (userQuest.getCompletedQuests().stream().anyMatch(completedQuest -> completedQuest.quest().equals(quest))) {
            return; // Exit if the quest is already completed
        }

        QuestStartEvent event = new QuestStartEvent(uuid, activeQuest);
        event.call();
        if (event.isCancelled()) return;

        // Add the active quest to the user's active quests
        userQuest.getActiveQuests().add(event.getActiveQuest());

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

        this.handleCustomReward(userQuest, activeQuest);
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
            this.addQuestToPlayer(player.getUniqueId(), quest);
            message(sender, Message.QUEST_START_SUCCESS, "%name%", questName, "%player%", player.getName());
        } else {
            message(sender, Message.QUEST_START_ERROR, "%name%", questName, "%player%", player.getName());
        }
    }

    @Override
    public void completeQuest(CommandSender sender, Player player, String questName) {

        ActiveQuest activeQuest = findActiveQuest(sender, player, questName);
        if (activeQuest == null) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        activeQuest.increment(activeQuest.getQuest().getGoal());
        getUserQuest(player.getUniqueId()).getActiveQuests().remove(activeQuest);
        completeQuest(activeQuest);

        message(sender, Message.QUEST_COMPLETE_SUCCESS, "%name%", questName, "%player%", player.getName());
    }

    @Override
    public void completeQuestGroup(CommandSender sender, Player player, String groupName) {

        var optional = getGroup(groupName);
        if (optional.isEmpty()) {
            message(sender, Message.GROUP_NOT_FOUND, "%name%", groupName);
            return;
        }

        var group = optional.get();
        for (Quest quest : group.getQuests()) {

            var userQuest = getUserQuest(player.getUniqueId());
            var optionalActiveQuest = userQuest.getActiveQuests().stream().filter(a -> a.getQuest() == quest).findFirst();

            if (optionalActiveQuest.isEmpty()) continue;

            var activeQuest = optionalActiveQuest.get();
            activeQuest.increment(activeQuest.getQuest().getGoal());
            getUserQuest(player.getUniqueId()).getActiveQuests().remove(activeQuest);
            completeQuest(activeQuest);
        }

        message(sender, Message.GROUP_COMPLETE_SUCCESS, "%name%", group.getDisplayName(), "%player%", player.getName());
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
    public void deleteUserQuests(CommandSender sender, Player player) {
        var userQuest = getUserQuest(player.getUniqueId());

        plugin.getStorageManager().deleteAll(player.getUniqueId());
        userQuest.getActiveQuests().clear();
        userQuest.getCompletedQuests().clear();
        handleDefaultQuest(player.getUniqueId());

        message(sender, Message.QUEST_DELETE_ALL_SUCCESS, "%player%", player.getName());
    }

    @Override
    public void setQuestProgress(CommandSender sender, Player player, String questName, int amount) {
        ActiveQuest activeQuest = findActiveQuest(sender, player, questName);
        if (activeQuest == null) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        activeQuest.setAmount(amount);
        this.plugin.getStorageManager().upsert(activeQuest);

        message(sender, Message.QUEST_SET_PROGRESS_SUCCESS, "%name%", questName, "%player%", player.getName(), "%progress%", amount);
    }

    @Override
    public void addQuestProgress(CommandSender sender, Player player, String questName, int amount) {
        ActiveQuest activeQuest = findActiveQuest(sender, player, questName);
        if (activeQuest == null) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        if (activeQuest.increment(amount)) {
            getUserQuest(player.getUniqueId()).getActiveQuests().remove(activeQuest);
            completeQuest(activeQuest);
        } else {
            this.plugin.getStorageManager().upsert(activeQuest);
        }

        message(sender, Message.QUEST_ADD_PROGRESS_SUCCESS, "%name%", questName, "%player%", player.getName(), "%progress%", amount);
    }

    private ActiveQuest findActiveQuest(CommandSender sender, Player player, String questName) {
        var userQuest = getUserQuest(player.getUniqueId());
        var optional = userQuest.getActiveQuests().stream().filter(a -> a.getQuest().getName().equalsIgnoreCase(questName)).findFirst();

        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return null;
        }

        return optional.get();
    }

    @Override
    public void openQuestInventory(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "quests");
    }

    @Override
    public List<CustomReward> getCustomRewards() {
        return customRewards;
    }

    private void handleCustomReward(UserQuest userQuest, ActiveQuest activeQuest) {

        Player player = Bukkit.getPlayer(activeQuest.getUniqueId());
        if (player == null) return;

        var quest = activeQuest.getQuest();
        Placeholders placeholders = new Placeholders();
        placeholders.register("name", quest.getDisplayName());
        placeholders.register("description", quest.getDescription());
        placeholders.register("goal", String.valueOf(quest.getGoal()));

        var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();

        for (Action action : this.globalRewards) {
            action.preExecute(player, null, fakeInventory, placeholders);
        }

        var optional = this.customRewards.stream().filter(customReward -> customReward.quests().contains(activeQuest.getQuest().getName())).findFirst();
        if (optional.isEmpty()) return;

        var customReward = optional.get();
        if (customReward.quests().stream().allMatch(userQuest::isQuestCompleted)) {
            for (Action action : customReward.actions()) {
                getaVoid(action, player, fakeInventory);
            }
        }
    }

    @Override
    public Map<String, QuestsGroup> getGroups() {
        return this.groups;
    }

    @Override
    public Optional<QuestsGroup> getGroup(String key) {
        return Optional.ofNullable(this.groups.get(key));
    }
}
