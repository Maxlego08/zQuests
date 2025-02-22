package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class ItemStackAction implements QuestAction {

    private final Material target;
    private final QuestType questType;
    private final int fireworkPower;

    public ItemStackAction(Material target, QuestType questType, int fireworkPower) {
        this.target = target;
        this.questType = questType;
        this.fireworkPower = fireworkPower;
    }

    @Override
    public QuestType getQuestType() {
        return questType;
    }

    @Override
    public boolean isAction(Object target) {
        if (target instanceof ItemStack itemStack) {

            if (itemStack.getType() != this.target) return false;
            System.out.println(itemStack);
            if (itemStack.hasItemMeta()) {
                var meta = itemStack.getItemMeta();
                if (meta instanceof FireworkMeta fireworkMeta && this.fireworkPower > 0) {
                    System.out.println(fireworkMeta.getPower() + " - " + fireworkPower +" -- " + (fireworkMeta.getPower() < this.fireworkPower));
                    if (fireworkMeta.getPower() < this.fireworkPower) return false;
                }
            }

            return true;
        }
        return false;
    }
}
