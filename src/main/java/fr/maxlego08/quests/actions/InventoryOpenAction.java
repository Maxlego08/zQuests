package fr.maxlego08.quests.actions;

import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

import java.util.List;

public class InventoryOpenAction implements QuestAction {

    private final List<String> nameInventories;

    public InventoryOpenAction(List<String> nameInventories) {
        this.nameInventories = nameInventories;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof Inventory inventory && this.nameInventories.contains(inventory.getFileName());
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.INVENTORY_OPEN;
    }
}
