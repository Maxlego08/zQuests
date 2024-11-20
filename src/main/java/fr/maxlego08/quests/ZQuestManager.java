package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.utils.Parameter;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
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
        return configuration.getMapList("quests").stream().map(map -> new TypedMapAccessor((Map<String, Object>) map)).map(typedMapAccessor -> this.loadQuest(typedMapAccessor, file)).filter(Objects::nonNull).toList();
    }

    private Quest loadQuest(TypedMapAccessor accessor, File file) {

        try {

            QuestType questType = QuestType.valueOf(accessor.getString("type", QuestType.BLOCK_BREAK.name()).toUpperCase());
            String name = accessor.getString("name");
            String displayName = accessor.getString("display-name", name);
            String description = accessor.getString("description", "no description");
            Material thumbnail = accessor.contains("thumbnail") ? Material.valueOf(accessor.getString("thumbnail").toUpperCase()) : null;
            long goal = accessor.getLong("goal");
            boolean autoAccept = accessor.getBoolean("auto-accept", false);

            List<Map<String, Object>> actions = accessor.contains("rewards") ? (List<Map<String, Object>>) accessor.getList("rewards") : new ArrayList<>();
            List<Action> rewards = plugin.getButtonManager().loadActions(actions, "quests", file);

            Quest quest = new ZQuest(this.plugin, name, questType, displayName, description, thumbnail, goal, autoAccept, rewards);

            Map<String, Object> parameters = new HashMap<>();
            switch (questType) {
                case BLOCK_BREAK, FARMING -> {
                    parameters.put("blocks", accessor.getStringList("blocks-types").stream().map(String::toUpperCase).map(Material::valueOf).toList());
                }
                case ENTITY_KILL -> {
                    parameters.put("entities", accessor.getStringList("entities-types").stream().map(String::toUpperCase).map(EntityType::valueOf).toList());
                }
            }
            quest.setParameters(parameters);

            return quest;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
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
    public void handleQuests(UUID uuid, QuestType type, int amount, Parameter<?>... parameters) {
        // Retrieve the user's quest data or create a new ZUserQuest if not found
        var userQuest = this.usersQuests.getOrDefault(uuid, new ZUserQuest());

        // Stream through the active quests of the user
        var iterator = userQuest.getActiveQuests().iterator();
        while (iterator.hasNext()) {
            ActiveQuest activeQuest = iterator.next();
            if (activeQuest.getQuest().getType() == type && !activeQuest.isComplete() && activeQuest.hasParameters(parameters)) {
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
