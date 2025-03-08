package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.InventoryContent;

public class InventoryContentAction extends ActionInfo<InventoryContent> {

    public InventoryContentAction(InventoryContent inventoryContent) {
        super(QuestType.INVENTORY_CONTENT, inventoryContent);
    }
}
