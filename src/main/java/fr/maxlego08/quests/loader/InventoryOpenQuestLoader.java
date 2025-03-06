package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.CustomAction;
import fr.maxlego08.quests.actions.InventoryOpenAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;

import java.io.File;
import java.util.List;

public class InventoryOpenQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public InventoryOpenQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        var inventories = accessor.getStringList("inventories");
        return new InventoryOpenAction(inventories);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.INVENTORY_OPEN);
    }
}
