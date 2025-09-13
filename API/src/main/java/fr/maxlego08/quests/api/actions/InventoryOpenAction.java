package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class InventoryOpenAction extends ActionInfo<String> {

    public InventoryOpenAction(String inventoryName) {
        super(QuestType.INVENTORY_OPEN, inventoryName);
    }
}
