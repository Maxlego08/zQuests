package fr.maxlego08.quests;

import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestsGroup;
import fr.maxlego08.quests.save.Config;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;
import java.util.function.Function;

/**
 * Handles quest group loading and lookups.
 */
public class QuestGroupManager {

    private final QuestsPlugin plugin;
    private final Map<String, QuestsGroup> groups = new HashMap<>();
    private int globalGroupCustomModelId;
    private String globalGroupDisplayName;

    public QuestGroupManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Load groups from configuration.
     */
    public void loadGroups() {
        this.groups.clear();

        FileConfiguration config = this.plugin.getInventoryManager().loadYamlConfiguration(new File(this.plugin.getDataFolder(), "config.yml"));

        this.globalGroupCustomModelId = config.getInt("global-group.custom-model-id", 0);
        this.globalGroupDisplayName = config.getString("global-group.display-name", "Global");

        var section = config.getConfigurationSection("quests-groups");
        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {
            var currentSection = section.getConfigurationSection(key);
            if (currentSection == null) continue;

            String displayName = currentSection.getString("display-name", key);
            int customModelId = currentSection.getInt("custom-model-id", 0);
            boolean isProgression = currentSection.getBoolean("progression", false);
            List<String> quests = currentSection.getStringList("quests");

            List<QuestsGroup> subGroups = new ArrayList<>();
            for (String subGroupName : currentSection.getStringList("sub-groups")) {
                getGroup(subGroupName).ifPresentOrElse(subGroups::add, () -> this.plugin.getLogger().severe("The group " + subGroupName + " doesn't exist !"));
            }

            this.groups.put(key, new ZQuestsGroup(key, displayName, quests, subGroups, customModelId, isProgression));
        }
    }

    /**
     * Update quests references inside groups using provided quest lookup.
     *
     * @param questProvider function returning optional quest for a name
     */
    public void updateGroups(Function<String, Optional<Quest>> questProvider) {
        for (QuestsGroup value : this.groups.values()) {
            value.setQuests(value.getQuestNames().stream()
                    .map(questProvider)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList());

            if (Config.enableDebug) {
                this.plugin.getLogger().info("Group " + value.getName() + " has " + value.getQuests().size() + " quests and " + value.getQuestNames().size() + " names");
            }

            if (value.getQuests().size() != value.getQuestNames().size()) {
                this.plugin.getLogger().severe("A group with the name " + value.getName() + " has a quest that doesn't exist. Quests expected: " + value.getQuestNames().size() + ", quests loaded: " + value.getQuests().size());
                for (String questName : value.getQuestNames()) {
                    if (questProvider.apply(questName).isEmpty()) {
                        this.plugin.getLogger().severe("The quest " + questName + " was not found !");
                    }
                }
            }
        }
    }

    public Map<String, QuestsGroup> getGroups() {
        return this.groups;
    }

    public Optional<QuestsGroup> getGroup(String key) {
        return Optional.ofNullable(this.groups.get(key));
    }

    public List<QuestsGroup> getGroups(Quest quest) {
        return this.groups.values().stream().filter(e -> e.getQuests().contains(quest)).toList();
    }

    public Optional<QuestsGroup> getGroup(Quest quest) {
        return this.groups.values().stream().filter(e -> e.getQuests().contains(quest) && e.isProgression()).findFirst();
    }

    public Optional<QuestsGroup> getGlobalGroup(Quest quest) {
        return this.groups.values().stream().filter(e -> e.getQuests().contains(quest) && !e.isProgression()).findFirst();
    }

    public int getGlobalGroupCustomModelId() {
        return this.globalGroupCustomModelId;
    }

    public String getGlobalGroupDisplayName() {
        return this.globalGroupDisplayName;
    }
}
