package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.EntityAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EntityQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public EntityQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        EntityType entityType = EntityType.valueOf(accessor.getString("entity").toUpperCase());
        return new EntityAction(entityType, questType);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return Arrays.stream(QuestType.values()).filter(QuestType::isEntityType).toList();
    }
}
