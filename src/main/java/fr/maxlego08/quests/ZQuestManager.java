package fr.maxlego08.quests;

import fr.maxlego08.menu.api.exceptions.InventoryException;
import fr.maxlego08.menu.api.loader.NoneLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.requirement.Permissible;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.InventoryContentAction;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.QuestsGroup;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.QuestEvent;
import fr.maxlego08.quests.api.event.events.QuestCompleteEvent;
import fr.maxlego08.quests.api.event.events.QuestDeleteAllEvent;
import fr.maxlego08.quests.api.event.events.QuestDeleteEvent;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeEvent;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeLimitEvent;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangePlaceholderTypeEvent;
import fr.maxlego08.quests.api.event.events.QuestPostProgressEvent;
import fr.maxlego08.quests.api.event.events.QuestProgressEvent;
import fr.maxlego08.quests.api.event.events.QuestStartEvent;
import fr.maxlego08.quests.api.event.events.QuestUserLoadEvent;
import fr.maxlego08.quests.api.utils.CustomReward;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.quests.api.utils.InventoryContent;
import fr.maxlego08.quests.api.utils.QuestHistory;
import fr.maxlego08.quests.api.utils.QuestInventoryPage;
import fr.maxlego08.quests.inventories.buttons.AddFavoriteLimitButton;
import fr.maxlego08.quests.inventories.buttons.ChangeFavoriteTypeButton;
import fr.maxlego08.quests.inventories.buttons.RemoveFavoriteLimitButton;
import fr.maxlego08.quests.inventories.loader.ChangeQuestGroupLoader;
import fr.maxlego08.quests.inventories.loader.QuestActiveLoader;
import fr.maxlego08.quests.inventories.loader.QuestCompleteLoader;
import fr.maxlego08.quests.inventories.loader.QuestFavoriteLoader;
import fr.maxlego08.quests.inventories.loader.QuestHistoryLoader;
import fr.maxlego08.quests.inventories.loader.QuestNotActiveLoader;
import fr.maxlego08.quests.inventories.loader.SetFavoriteLimitLoader;
import fr.maxlego08.quests.inventories.loader.SetFavoriteTypeLoader;
import fr.maxlego08.quests.inventories.loader.StartQuestLoader;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.zcore.utils.QuestPlaceholderUtil;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ZQuestManager extends ZUtils implements QuestManager {

    private final QuestsPlugin plugin;
    private final List<Quest> quests = new ArrayList<>();
    private final Map<UUID, UserQuest> usersQuests = new HashMap<>();
    private final List<CustomReward> customRewards = new ArrayList<>();
    private final QuestGroupManager groupManager;

    private List<Action> globalRewards = new ArrayList<>();
    private List<Permissible> globalPermissibles = new ArrayList<>();

    public ZQuestManager(QuestsPlugin plugin) {
        this.plugin = plugin;
        this.groupManager = new QuestGroupManager(plugin);
    }

    @Override
    public void loadButtons() {
        var buttonManager = this.plugin.getButtonManager();
        buttonManager.registerAction(new StartQuestLoader(this.plugin));
        buttonManager.register(new QuestCompleteLoader(this.plugin));
        buttonManager.register(new QuestActiveLoader(this.plugin));
        buttonManager.register(new QuestNotActiveLoader(this.plugin));
        buttonManager.register(new QuestHistoryLoader(this.plugin));
        buttonManager.register(new ChangeQuestGroupLoader(this.plugin));
        buttonManager.register(new QuestFavoriteLoader(this.plugin));
        buttonManager.register(new SetFavoriteLimitLoader(this.plugin));
        buttonManager.register(new SetFavoriteTypeLoader(this.plugin));
        buttonManager.register(new NoneLoader(this.plugin, ChangeFavoriteTypeButton.class, "ZQUESTS_CHANGE_FAVORITE_TYPE"));
        buttonManager.register(new NoneLoader(this.plugin, AddFavoriteLimitButton.class, "ZQUESTS_ADD_FAVORITE_LIMIT"));
        buttonManager.register(new NoneLoader(this.plugin, RemoveFavoriteLimitButton.class, "ZQUESTS_REMOVE_FAVORITE_LIMIT"));
    }

    @Override
    public void loadPatterns() {
        var patternManager = this.plugin.getPatternManager();

        File folder = new File(this.plugin.getDataFolder(), "patterns");
        if (!folder.exists()) {
            folder.mkdirs();
            this.plugin.saveResource("patterns/quest-display.yml", false);
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
            this.plugin.saveResource("inventories/quests-history.yml", false);
            this.plugin.saveResource("inventories/quests-options.yml", false);
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
            this.plugin.saveResource("quests/example_complex.yml", false);
        }

        // Load holograms
        this.plugin.getHologramManager().loadGlobalConfiguration();
        this.plugin.getWayPointManager().loadGlobalConfiguration();

        // Load groups before quests
        this.groupManager.loadGroups();

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

        this.groupManager.updateGroups(this::getQuest);
    }

    private void updateOnlyPlayers() {

        var playTimeHelper = this.plugin.getPlayTimeHelper();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UserQuest userQuest = this.getUserQuest(player.getUniqueId());
            List<ActiveQuest> activeQuests = userQuest.getActiveQuests();

            for (int i = 0; i < activeQuests.size(); i++) {
                ActiveQuest activeQuest = activeQuests.get(i);
                Optional<Quest> optionalQuest = this.getQuest(activeQuest.getQuest().getName());
                if (optionalQuest.isPresent()) {
                    Quest quest = optionalQuest.get();
                    activeQuests.set(i, new ZActiveQuest(this.plugin, activeQuest.getPlayerUUID(), quest, activeQuest.getCreatedAt(), activeQuest.getAmount(), activeQuest.isFavorite(), playTimeHelper.getPlayTime(player.getUniqueId())));
                }
            }

            List<CompletedQuest> completedQuests = userQuest.getCompletedQuests();
            for (int i = 0; i < completedQuests.size(); i++) {
                CompletedQuest completedQuest = completedQuests.get(i);
                Optional<Quest> optionalQuest = this.getQuest(completedQuest.quest().getName());
                if (optionalQuest.isPresent()) {
                    Quest quest = optionalQuest.get();
                    completedQuests.set(i, new CompletedQuest(quest, completedQuest.completedAt(), completedQuest.startedAt(), completedQuest.isFavorite(), completedQuest.startPlayTime(), completedQuest.completPlayTime()));
                }
            }
        }
    }

    private void loadGlobalRewards() {
        FileConfiguration configuration = this.plugin.getConfig();
        var manager = this.plugin.getButtonManager();
        var file = new File(this.plugin.getDataFolder(), "config.yml");

        var globalRewardConfiguration = configuration.getList("global-rewards");
        if (globalRewardConfiguration != null) {
            this.globalRewards = manager.loadActions((List<Map<String, Object>>) globalRewardConfiguration, "global-rewards", file);
        }

        var globalPermissibleConfiguration = configuration.getList("global-permissibles");
        if (globalPermissibleConfiguration != null) {
            this.globalPermissibles = manager.loadPermissible((List<Map<String, Object>>) globalPermissibleConfiguration, "global-permissibles", file);
        }
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

            this.plugin.getScheduler().runNextTick(w -> {

                var iterator = userQuest.getActiveQuests().listIterator();

                while (iterator.hasNext()) {
                    ActiveQuest activeQuest = iterator.next();
                    if (activeQuest.isComplete()) {
                        iterator.remove();
                        activeQuest.getQuest().onComplete(activeQuest);
                        this.completeQuest(activeQuest);
                    }
                }

                handleDefaultQuest(player.getUniqueId());
            });
        });
    }

    /**
     * Handle default quests for a player.
     *
     * <p>This method finds all quests that are auto-accepted and that the player
     * has not yet completed, and starts them for the player.</p>
     *
     * @param uuid the UUID of the player
     */
    private void handleDefaultQuest(UUID uuid) {

        UserQuest userQuest = getUserQuest(uuid);
        var quests = this.quests.stream().filter(Quest::isAutoAccept).filter(quest -> userQuest.getActiveQuests().stream().noneMatch(activeQuest -> activeQuest.getQuest().equals(quest)));
        this.startQuests(uuid, quests.toList());

        this.callQuestEvent(uuid, new QuestUserLoadEvent(userQuest));
    }

    @Override
    public void startQuests(UUID uuid, List<Quest> quests) {
        List<ActiveQuest> activeQuests = new ArrayList<>();
        for (Quest quest : quests) {
            this.addQuestToPlayer(uuid, quest, false).ifPresent(activeQuests::add);
        }
        this.plugin.getStorageManager().upsert(activeQuests);
    }

    @Override
    public void handleQuit(UUID playerUUID) {
        UserQuest userQuest = this.usersQuests.remove(playerUUID);
        if (userQuest == null) return;
        // userQuest.getActiveQuests().forEach(activeQuest -> this.plugin.getStorageManager().upsert(activeQuest));
        this.plugin.getStorageManager().upsert(userQuest.getActiveQuests());
    }

    @Override
    public Set<ActiveQuest> handleQuests(UUID playerUUID, QuestType type, int amount, Object object, Consumer<ActiveQuest> consumer, boolean isStatic) {

        Set<ActiveQuest> activeQuests = new HashSet<>();
        // Retrieve the user's quest data or create a new ZUserQuest if not found
        var userQuest = getUserQuest(playerUUID);

        Consumer<ActiveQuest> after = activeQuest -> {

            QuestCompleteEvent completeEvent = new QuestCompleteEvent(playerUUID, activeQuest);
            if (callQuestEvent(playerUUID, completeEvent)) return;

            userQuest.removeActiveQuest(activeQuest);
            this.completeQuest(activeQuest);

            if (consumer != null) {
                consumer.accept(activeQuest);
            }
        };

        // Stream through the active quests of the user
        for (ActiveQuest activeQuest : new ArrayList<>(userQuest.getActiveQuests())) {
            if (activeQuest.getQuest().getType() == type && !activeQuest.isComplete() && activeQuest.isQuestAction(object)) {

                // Check if player can complete the quest
                if (!activeQuest.canComplete()) continue;

                QuestProgressEvent progressEvent = new QuestProgressEvent(playerUUID, activeQuest, amount);
                if (callQuestEvent(playerUUID, progressEvent)) continue;

                amount = progressEvent.getAmount();

                if (isStatic) {
                    if (activeQuest.incrementStatic(amount)) {
                        after.accept(activeQuest);
                    }
                } else {
                    if (activeQuest.increment(amount)) {
                        after.accept(activeQuest);
                    }
                }
                activeQuests.add(activeQuest);
                this.plugin.getStorageManager().softUpsert(activeQuest); // Soft update the quest in storage

                // If the quest is unique, stop the others.
                // This allows you to only have one active quest at a time.
                // If you have multiple quests that are triggered by the same action (e.g. talking to a citizen),
                // you can use this method to ensure that only one of them is active at a time.
                if (activeQuest.getQuest().isUnique()) {
                    break;
                }
            }
        }

        if (!activeQuests.isEmpty()) {
            callQuestEvent(playerUUID, new QuestPostProgressEvent(playerUUID, activeQuests));
        }

        return activeQuests;

    }

    @Override
    public Set<ActiveQuest> handleQuests(UUID playerUUID, QuestType type, int amount, Object object, Consumer<ActiveQuest> consumer) {
        return this.handleQuests(playerUUID, type, amount, object, consumer, false);
    }

    @Override
    public Set<ActiveQuest> handleQuests(UUID playerUUID, QuestType type, int amount, Object object) {
        return this.handleQuests(playerUUID, type, amount, object, null);
    }

    @Override
    public Set<ActiveQuest> handleStaticQuests(UUID playerUUID, QuestType type, int amount, Object object) {
        return this.handleStaticQuests(playerUUID, type, amount, object, null);
    }

    @Override
    public Set<ActiveQuest> handleStaticQuests(UUID playerUUID, QuestType type, int amount, Object object, Consumer<ActiveQuest> consumer) {
        return this.handleQuests(playerUUID, type, amount, object, consumer, true);
    }

    @Override
    public Set<ActiveQuest> handleInventoryQuests(InventoryContent inventoryContent) {

        Set<ActiveQuest> activeQuests = new HashSet<>();
        var player = inventoryContent.player();

        // Retrieve the user's quest data or create a new ZUserQuest if not found
        var userQuest = getUserQuest(player.getUniqueId());

        for (ActiveQuest activeQuest : new ArrayList<>(userQuest.getActiveQuests())) {
            if (activeQuest.getQuest().getType() == QuestType.INVENTORY_CONTENT && !activeQuest.isComplete() && activeQuest.isQuestAction(inventoryContent)) {

                var optional = activeQuest.getQuest().getActions().stream().filter(e -> e instanceof InventoryContentAction).map(e -> (InventoryContentAction) e).findFirst();
                if (optional.isEmpty()) continue;

                var inventoryContentAction = optional.get();
                int amount = inventoryContentAction.countItems(player);
                if (amount == 0) continue;

                // Check if player can complete the quest
                if (!activeQuest.canComplete()) continue;

                QuestProgressEvent progressEvent = new QuestProgressEvent(player.getUniqueId(), activeQuest, amount);
                if (callQuestEvent(player.getUniqueId(), progressEvent)) continue;

                long before = activeQuest.getAmount();
                amount = progressEvent.getAmount();

                if (activeQuest.increment(amount)) { // Increment the progress of the quest

                    QuestCompleteEvent completeEvent = new QuestCompleteEvent(player.getUniqueId(), activeQuest);
                    if (callQuestEvent(player.getUniqueId(), completeEvent)) continue;

                    userQuest.removeActiveQuest(activeQuest);
                    this.completeQuest(activeQuest);
                    amount = (int) (activeQuest.getQuest().getGoal() - before);
                }

                activeQuests.add(activeQuest);
                inventoryContentAction.removeItems(player, amount);
                this.plugin.getStorageManager().softUpsert(activeQuest); // Soft update the quest in storage

                // If the quest is unique, stop the others.
                // This allows you to only have one active quest at a time.
                // If you have multiple quests that are triggered by the same action (e.g. talking to a citizen),
                // you can use this method to ensure that only one of them is active at a time.
                if (activeQuest.getQuest().isUnique()) {
                    break;
                }
            }
        }

        if (!activeQuests.isEmpty()) {
            callQuestEvent(player.getUniqueId(), new QuestPostProgressEvent(player.getUniqueId(), activeQuests));
        }

        return activeQuests;
    }

    @Override
    public Optional<ActiveQuest> addQuestToPlayer(UUID playerUUID, Quest quest, boolean store) {

        var userQuest = getUserQuest(playerUUID);

        boolean isFavorite = quest.isFavorite();
        if (!isFavorite) {
            var optional = getGroup(quest);
            if (optional.isPresent()) {
                var group = optional.get();
                isFavorite = group.needFavorite(quest, userQuest);
            }
        }

        // Create a new active quest for the player
        ActiveQuest activeQuest = new ZActiveQuest(this.plugin, playerUUID, quest, new Date(), 0, isFavorite, this.plugin.getPlayTimeHelper().getPlayTime(playerUUID));

        // Check if the user already completes the quest
        if (userQuest.getCompletedQuests().stream().anyMatch(completedQuest -> completedQuest.quest().equals(quest))) {
            return Optional.empty(); // Exit if the quest is already completed
        }

        QuestStartEvent event = new QuestStartEvent(playerUUID, activeQuest);
        if (callQuestEvent(playerUUID, event)) return Optional.empty();

        // Add the active quest to the user's active quests
        userQuest.getActiveQuests().add(event.getActiveQuest());

        var offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        if (offlinePlayer.isOnline()) {
            var player = offlinePlayer.getPlayer();
            if (player != null) {

                var placeholders = QuestPlaceholderUtil.createPlaceholder(this.plugin, player, quest);
                var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();

                for (Action action : quest.getStartActions()) {
                    action.preExecute(player, null, fakeInventory, placeholders);
                }
            }
        }

        // Persist the new active quest in storage
        if (store) {
            this.plugin.getStorageManager().upsert(activeQuest);
        }
        return Optional.of(activeQuest);
    }

    @Override
    public List<QuestsGroup> getGroups(Quest quest) {
        return this.groupManager.getGroups(quest);
    }

    @Override
    public List<ActiveQuest> getQuestsFromPlayer(UUID playerUUID) {
        return getUserQuest(playerUUID).getActiveQuests();
    }

    @Override
    public Optional<Quest> getQuest(String name) {
        return this.quests.stream().filter(quest -> quest.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public void completeQuest(ActiveQuest activeQuest) {

        var userQuest = getUserQuest(activeQuest.getPlayerUUID());

        CompletedQuest completedQuest = activeQuest.complete();
        userQuest.getCompletedQuests().add(completedQuest);
        this.plugin.getStorageManager().insert(activeQuest.getPlayerUUID(), completedQuest);
        this.plugin.getStorageManager().delete(activeQuest);

        this.handleCustomReward(userQuest, activeQuest);
    }

    @Override
    public UserQuest getUserQuest(UUID uuid) {
        return this.usersQuests.computeIfAbsent(uuid, ZUserQuest::new);
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

        if (userQuest.isQuestActive(quest)) {
            message(sender, Message.QUEST_START_ERROR_ACTIVE, "%name%", questName, "%player%", player.getName());
            return;
        }

        if (userQuest.isQuestCompleted(quest)) {
            message(sender, Message.QUEST_START_ERROR_COMPLETED, "%name%", questName, "%player%", player.getName());
            return;
        }

        this.addQuestToPlayer(player.getUniqueId(), quest, true);
        message(sender, Message.QUEST_START_SUCCESS, "%name%", questName, "%player%", player.getName());
    }

    @Override
    public void activateQuestGroup(CommandSender sender, Player player, String groupName) {
        var optional = getGroup(groupName);
        if (optional.isEmpty()) {
            message(sender, Message.GROUP_NOT_FOUND, "%name%", groupName);
            return;
        }

        var group = optional.get();
        UserQuest userQuest = getUserQuest(player.getUniqueId());

        for (Quest quest : group.getQuests()) {
            if (userQuest.canStartQuest(quest)) {
                this.addQuestToPlayer(player.getUniqueId(), quest, true);
            }
        }
        message(sender, Message.QUEST_START_GROUP, "%name%", groupName, "%player%", player.getName());
    }

    @Override
    public void completeQuest(CommandSender sender, Player player, String questName) {

        ActiveQuest activeQuest = findActiveQuest(sender, player, questName);
        if (activeQuest == null) return;

        if (callQuestEvent(player.getUniqueId(), new QuestCompleteEvent(player.getUniqueId(), activeQuest))) {
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

        Set<ActiveQuest> activeQuests = new HashSet<>();

        for (Quest quest : group.getQuests()) {

            var userQuest = getUserQuest(player.getUniqueId());
            var optionalActiveQuest = userQuest.getActiveQuests().stream().filter(a -> a.getQuest() == quest).findFirst();

            if (optionalActiveQuest.isEmpty()) continue;

            var activeQuest = optionalActiveQuest.get();

            if (callQuestEvent(player.getUniqueId(), new QuestCompleteEvent(player.getUniqueId(), activeQuest))) {
                continue;
            }

            activeQuest.increment(activeQuest.getQuest().getGoal());
            getUserQuest(player.getUniqueId()).getActiveQuests().remove(activeQuest);
            completeQuest(activeQuest);
            activeQuests.add(activeQuest);
        }

        this.callQuestEvent(player.getUniqueId(), new QuestPostProgressEvent(player.getUniqueId(), activeQuests));

        message(sender, Message.GROUP_COMPLETE_SUCCESS, "%name%", group.getDisplayName(), "%player%", player.getName());
    }

    @Override
    public void deleteUserQuest(CommandSender sender, Player player, String questName) {

        // ToDo, rework with offline player

        var userQuest = getUserQuest(player.getUniqueId());

        Optional<Quest> optional = this.getQuest(questName);
        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        var quest = optional.get();

        this.callQuestEvent(player.getUniqueId(), new QuestDeleteEvent(userQuest, quest));

        userQuest.getActiveQuests().removeIf(activeQuest -> activeQuest.getQuest() == quest);
        userQuest.getCompletedQuests().removeIf(completedQuest -> completedQuest.quest() == quest);

        this.plugin.getStorageManager().deleteQuest(player.getUniqueId(), quest.getName());

        message(sender, Message.QUEST_DELETE_SUCCESS, "%name%", questName, "%player%", player.getName());
    }

    @Override
    public void deleteUserQuests(CommandSender sender, Player player) {

        // ToDo, rework with offline player

        var userQuest = getUserQuest(player.getUniqueId());

        this.plugin.getStorageManager().deleteAll(player.getUniqueId(), () -> this.plugin.getScheduler().runNextTick(w -> {

            this.callQuestEvent(player.getUniqueId(), new QuestDeleteAllEvent(userQuest));

            userQuest.getActiveQuests().clear();
            userQuest.getCompletedQuests().clear();
            handleDefaultQuest(player.getUniqueId());

            message(sender, Message.QUEST_DELETE_ALL_SUCCESS, "%player%", player.getName());
        }));
    }

    @Override
    public void setQuestProgress(CommandSender sender, Player player, String questName, int amount) {
        ActiveQuest activeQuest = findActiveQuest(sender, player, questName);
        if (activeQuest == null) return;

        activeQuest.setAmount(amount);
        this.plugin.getStorageManager().upsert(activeQuest);

        this.callQuestEvent(player.getUniqueId(), new QuestPostProgressEvent(player.getUniqueId(), Set.of(activeQuest)));

        message(sender, Message.QUEST_SET_PROGRESS_SUCCESS, "%name%", questName, "%player%", player.getName(), "%progress%", amount);
    }

    @Override
    public void addQuestProgress(CommandSender sender, Player player, String questName, int amount) {
        ActiveQuest activeQuest = findActiveQuest(sender, player, questName);
        if (activeQuest == null) return;

        if (activeQuest.increment(amount)) {
            getUserQuest(player.getUniqueId()).getActiveQuests().remove(activeQuest);
            completeQuest(activeQuest);
        } else {
            this.plugin.getStorageManager().upsert(activeQuest);
        }

        this.callQuestEvent(player.getUniqueId(), new QuestPostProgressEvent(player.getUniqueId(), Set.of(activeQuest)));

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
    public void openQuestInventory(Player player, QuestInventoryPage questInventoryPage) {
        var manager = this.plugin.getInventoryManager();
        manager.getInventory(this.plugin, questInventoryPage.inventory()).ifPresentOrElse(inventory -> manager.openInventory(player, inventory, questInventoryPage.page()), () -> message(player, Message.INVENTORY_NOT_FOUND, "%inventory%", questInventoryPage.inventory()));
    }

    @Override
    public List<CustomReward> getCustomRewards() {
        return customRewards;
    }

    /**
     * Handle custom rewards for the given {@link UserQuest} and {@link ActiveQuest}.
     * This method will execute the custom rewards actions for the given active quest.
     * The custom rewards will be executed only if the player has completed all the quests
     * required by the custom reward.
     *
     * @param userQuest   the user quest
     * @param activeQuest the active quest
     */
    private void handleCustomReward(UserQuest userQuest, ActiveQuest activeQuest) {

        Player player = Bukkit.getPlayer(activeQuest.getPlayerUUID());
        if (player == null) {
            this.plugin.getLogger().severe("[CUSTOM REWARD] Player not found: " + activeQuest.getPlayerUUID() + ", unable to handle custom reward for quest " + activeQuest.getQuest().getName());
            return;
        }

        var quest = activeQuest.getQuest();
        Placeholders placeholders = new Placeholders();
        placeholders.register("name", quest.getDisplayName());
        placeholders.register("description", quest.getDescription());
        placeholders.register("goal", String.valueOf(quest.getGoal()));

        var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();

        if (quest.useGlobalRewards()) {
            for (Action action : this.globalRewards) {
                action.preExecute(player, null, fakeInventory, placeholders);
            }

            for (Permissible permissible : this.globalPermissibles) {
                var actions = permissible.hasPermission(player, null, fakeInventory, placeholders) ? permissible.getSuccessActions() : permissible.getDenyActions();
                actions.forEach(action -> action.preExecute(player, null, fakeInventory, placeholders));
            }
        }

        var optional = this.customRewards.stream().filter(customReward -> customReward.quests().contains(activeQuest.getQuest().getName())).findFirst();
        if (optional.isEmpty()) return;

        var customReward = optional.get();
        if (customReward.quests().stream().allMatch(userQuest::isQuestCompleted)) {
            for (Action action : customReward.actions()) {
                action.preExecute(player, null, fakeInventory, new Placeholders());
            }
        }
    }

    @Override
    public Map<String, QuestsGroup> getGroup() {
        return this.groupManager.getGroups();
    }

    @Override
    public Optional<QuestsGroup> getGroup(String key) {
        return this.groupManager.getGroup(key);
    }

    @Override
    public void setFavorite(CommandSender sender, Player player, String questName, boolean newValue) {

        var userQuest = getUserQuest(player.getUniqueId());

        var optional = userQuest.findActive(questName);
        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        var activeQuest = optional.get();
        if (!activeQuest.getQuest().canChangeFavorite()) {
            message(sender, Message.QUEST_CANT_CHANGE_FAVORITE, "%name%", questName);
            return;
        }

        QuestFavoriteChangeEvent event = new QuestFavoriteChangeEvent(player, activeQuest, newValue);
        if (callQuestEvent(player.getUniqueId(), event)) return;

        activeQuest.setFavorite(event.isFavorite());
        this.plugin.getStorageManager().upsert(activeQuest);

        message(sender, Message.QUEST_SET_FAVORITE_SUCCESS, "%name%", questName, "%player%", player.getName(), "%favorite%", newValue);
    }

    @Override
    public boolean callQuestEvent(UUID playerUniqueId, QuestEvent event) {

        var configuration = Config.eventConfigurations.get(event.getClass());

        boolean isCancelled = false;

        if (configuration != null) {

            if (configuration.enabled()) {
                isCancelled = !event.callEvent();
            }

            if ((configuration.updateScoreboard() || configuration.updateHologram() || configuration.updateWayPoint()) && playerUniqueId != null) {
                this.plugin.getScheduler().runLater(w -> {

                    if (configuration.updateScoreboard()) {
                        this.plugin.getScoreboardHook().updateScoreboard(playerUniqueId);
                    }

                    if (configuration.updateHologram()) {
                        this.plugin.getHologramManager().onQuestEvent(event);
                    }

                    if (configuration.updateWayPoint()) {
                        this.plugin.getWayPointManager().onQuestEvent(event);
                    }
                }, Config.eventTicks);
            }
        }

        return isCancelled;
    }

    @Override
    public void restartUserQuest(CommandSender sender, OfflinePlayer player, String questName) {

        var userQuest = getUserQuest(player.getUniqueId());

        var optional = userQuest.findComplete(questName);
        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        var completedQuest = optional.get();
        userQuest.getCompletedQuests().remove(completedQuest);
        this.plugin.getStorageManager().delete(player.getUniqueId(), completedQuest);

        message(sender, Message.QUEST_RESTART_SUCCESS, "%name%", questName, "%player%", player.getName());
    }

    @Override
    public List<QuestHistory> getDisplayQuests(Player player) {
        var userQuests = this.getUserQuest(player.getUniqueId());
        var currentGroup = userQuests.getCurrentGroup();

        List<QuestHistory> quests = new ArrayList<>();
        quests.addAll(userQuests.getActiveQuests().stream().filter(e -> !e.getQuest().isHidden() && isQuestGroup(e.getQuest(), currentGroup)).map(e -> new QuestHistory(e, null)).toList());
        quests.addAll(userQuests.getCompletedQuests().stream().filter(e -> isQuestGroup(e.quest(), currentGroup)).map(e -> new QuestHistory(null, e)).toList());

        return quests.stream() //
                .sorted(Comparator.comparingInt(QuestHistory::sortActive)) // Sort Active
                .sorted(Comparator.comparingInt(QuestHistory::sortFav).reversed()) // Sort Fav
                .sorted(Comparator.comparingInt(QuestHistory::sortCompletedDate)) // Sort completed date
                .toList();
    }

    @Override
    public int getGlobalGroupCustomModelId() {
        return this.groupManager.getGlobalGroupCustomModelId();
    }

    @Override
    public String getGlobalGroupDisplayName() {
        return this.groupManager.getGlobalGroupDisplayName();
    }

    @Override
    public void showQuests(CommandSender sender, OfflinePlayer offlinePlayer) {

        if (this.usersQuests.containsKey(offlinePlayer.getUniqueId())) {
            showQuests(sender, offlinePlayer, this.usersQuests.get(offlinePlayer.getUniqueId()));
        } else {
            message(sender, Message.SHOW_LOAD_USER, "%player%", offlinePlayer.getName());
            this.plugin.getStorageManager().load(offlinePlayer.getUniqueId(), userQuest -> showQuests(sender, offlinePlayer, userQuest));
        }
    }

    @Override
    public void setFavoriteLimit(CommandSender sender, OfflinePlayer offlinePlayer, int amount) {

        var userQuest = this.usersQuests.get(offlinePlayer.getUniqueId());

        var event = new QuestFavoriteChangeLimitEvent(userQuest, amount);
        if (callQuestEvent(offlinePlayer.getUniqueId(), event)) return;

        userQuest.setFavoriteLimit(event.getNewLimit());

        this.plugin.getStorageManager().upsertPlayerFavoriteQuestConfiguration(offlinePlayer.getUniqueId(), event.getNewLimit(), userQuest.getFavoritePlaceholderType());
        message(sender, Message.QUEST_SET_FAVORITE_LIMIT_SUCCESS, "%player%", offlinePlayer.getName(), "%limit%", event.getNewLimit());
    }

    @Override
    public void setFavoriteType(CommandSender sender, OfflinePlayer offlinePlayer, FavoritePlaceholderType favoritePlaceholderType) {

        var userQuest = this.usersQuests.get(offlinePlayer.getUniqueId());

        var event = new QuestFavoriteChangePlaceholderTypeEvent(userQuest, favoritePlaceholderType);
        if (callQuestEvent(offlinePlayer.getUniqueId(), event)) return;

        userQuest.setFavoritePlaceholderType(event.getNewFavoritePlaceholderType());

        this.plugin.getStorageManager().upsertPlayerFavoriteQuestConfiguration(offlinePlayer.getUniqueId(), userQuest.getFavoriteLimit(), event.getNewFavoritePlaceholderType());
        message(sender, Message.QUEST_SET_FAVORITE_TYPE_SUCCESS, "%player%", offlinePlayer.getName(), "%type%", name(event.getNewFavoritePlaceholderType().name()));

    }

    @Override
    public void giveQuestReward(CommandSender sender, Player player, String questName) {

        var optional = getQuest(questName);
        if (optional.isEmpty()) {
            message(sender, Message.QUEST_NOT_FOUND, "%name%", questName);
            return;
        }

        var quest = optional.get();
        var fakeActiveQuest = new ZActiveQuest(this.plugin, player.getUniqueId(), quest, new Date(), quest.getGoal(), false, 0);
        quest.onComplete(fakeActiveQuest);

        message(sender, Message.QUEST_REWARD, "%name%", quest.getName(), "%player%", player.getName());
    }

    /**
     * Displays the active and completed quests of a specific offline player.
     *
     * @param sender        the command sender requesting the quest information
     * @param offlinePlayer the offline player whose quests are being displayed
     * @param userQuest     the user's quest data containing active and completed quests
     */
    private void showQuests(CommandSender sender, OfflinePlayer offlinePlayer, UserQuest userQuest) {

        var activeQuests = userQuest.getActiveQuests();
        var completedQuests = userQuest.getCompletedQuests();

        var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message(sender, Message.SHOW_HEADER, "%player%", offlinePlayer.getName(), "%active-quests%", activeQuests.size(), "%completed-quests%", completedQuests.size());
        var quests = activeQuests.stream().map(activeQuest -> {

            var placeholders = QuestPlaceholderUtil.createPlaceholder(plugin, null, activeQuest.getQuest());
            placeholders.register("started-at", format.format(activeQuest.getCreatedAt()));
            placeholders.register("amount", format(activeQuest.getAmount()));
            placeholders.register("is-favorite", activeQuest.isFavorite() ? "<green>true" : "<red>false");

            return placeholders.parse(Message.SHOW_ELEMENT.msg());
        }).collect(Collectors.joining(", "));

        message(sender, Message.SHOW_INFOS, "%quests%", quests);
    }

    /**
     * Returns true if the quest belongs to the given group name, false otherwise.
     * If the group name is null, the method will return true.a
     *
     * @param quest            the quest to be checked
     * @param currentGroupName the name of the group to be checked
     * @return true if the quest belongs to the given group name, false otherwise
     */
    private boolean isQuestGroup(Quest quest, String currentGroupName) {
        var groups = getGroups(quest);
        return currentGroupName == null || groups.stream().anyMatch(e -> e.getName().equalsIgnoreCase(currentGroupName));
    }

    @Override
    public Optional<QuestsGroup> getGroup(Quest quest) {
        return this.groupManager.getGroup(quest);
    }

    @Override
    public Optional<QuestsGroup> getGlobalGroup(Quest quest) {
        return this.groupManager.getGlobalGroup(quest);
    }
}
