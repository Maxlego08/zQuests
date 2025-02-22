package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.inventory.ItemStack;

public class ItemStackAction extends ActionInfo<ItemStack> {
    public ItemStackAction(QuestType questType, ItemStack value) {
        super(questType, value);
    }
}
