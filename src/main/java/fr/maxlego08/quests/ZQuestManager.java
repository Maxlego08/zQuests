package fr.maxlego08.quests;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.zcore.utils.ZUtils;
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
    public void loadQuests() {

        File folder = new File(this.plugin.getDataFolder(), "quests");
        if (!folder.exists()) {
            folder.mkdirs();
            this.plugin.saveResource("quests/example_blocks.yml", false);
            this.plugin.saveResource("quests/example_entities.yml", false);
            this.plugin.saveResource("quests/example_farming.yml", false);
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
        var userQuest = this.usersQuests.getOrDefault(uuid, new ZUserQuest());

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
        var userQuest = this.usersQuests.computeIfAbsent(player.getUniqueId(), uuid -> new ZUserQuest());

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
        return this.usersQuests.getOrDefault(uuid, new ZUserQuest()).getActiveQuests();
    }

    @Override
    public Optional<Quest> getQuest(String name) {
        return this.quests.stream().filter(quest -> quest.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public void completeQuest(ActiveQuest activeQuest) {

        var userQuest = this.usersQuests.computeIfAbsent(activeQuest.getUniqueId(), uuid -> new ZUserQuest());

        CompletedQuest completedQuest = activeQuest.complete();
        userQuest.getCompletedQuests().add(completedQuest);
        this.plugin.getStorageManager().upsert(activeQuest.getUniqueId(), completedQuest);
        this.plugin.getStorageManager().delete(activeQuest);
    }


    public void activateQuest(CommandSender sender, Player player, String questName) {
        Optional<Quest> optionalQuest = this.getQuest(questName);
        if (optionalQuest.isPresent()) {
            Quest quest = optionalQuest.get();
            if (this.getQuestsFromPlayer(player.getUniqueId()).stream().noneMatch(activeQuest -> activeQuest.getQuest().equals(quest))) {
                this.addQuestToPlayer(player, quest);
                sender.sendMessage("La qu te " + questName + " a  t  activ e pour le joueur " + player.getName());
            } else {
                sender.sendMessage("Le joueur " + player.getName() + " a d j  cette qu te en cours d'execution");
            }
        } else {
            sender.sendMessage("La qu te " + questName + " n'existe pas");
        }
    }
}
