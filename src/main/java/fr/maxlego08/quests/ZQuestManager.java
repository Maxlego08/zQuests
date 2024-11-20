package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ZQuestManager extends ZUtils implements QuestManager {

    private final QuestsPlugin plugin;
    private final List<Quest> quests = new ArrayList<>();

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
                case BLOCK_BREAK -> {
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
}
