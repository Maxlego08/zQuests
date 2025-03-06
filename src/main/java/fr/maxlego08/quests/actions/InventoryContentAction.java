package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryContentAction implements QuestAction {

    private final Material material;
    private final Tag<Material> materialTag;
    private final int customModelId;

    public InventoryContentAction(Material material, Tag<Material> materialTag, int customModelId) {
        this.material = material;
        this.materialTag = materialTag;
        this.customModelId = customModelId;
    }

    private boolean needItemMeta() {
        return this.customModelId != 0;
    }

    @Override
    public boolean isAction(Object target) {

        if (target instanceof ItemStack itemStack) {

            if (this.material != null && itemStack.getType() != this.material) return false;
            if (this.materialTag != null && !this.materialTag.isTagged(itemStack.getType())) return false;

            if (this.needItemMeta()) {

                if (!itemStack.hasItemMeta()) return false;

                ItemMeta itemMeta = itemStack.getItemMeta();
                if (this.customModelId != 0 && itemMeta.getCustomModelData() != this.customModelId) return false;

            }

            return true;
        }

        return false;
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.INVENTORY_CONTENT;
    }
}
