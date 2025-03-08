package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.InventoryContent;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryContentAction implements QuestAction {

    private final String citizenName;
    private final Material material;
    private final Tag<Material> materialTag;
    private final int customModelId;

    public InventoryContentAction(String citizenName, Material material, Tag<Material> materialTag, int customModelId) {
        this.citizenName = citizenName;
        this.material = material;
        this.materialTag = materialTag;
        this.customModelId = customModelId;
    }

    private boolean needItemMeta() {
        return this.customModelId != 0;
    }

    @Override
    public boolean isAction(Object target) {

        if (target instanceof InventoryContent inventoryContent) {

            Player player = inventoryContent.player();

            if (inventoryContent.citizenName() != null && !inventoryContent.citizenName().equals(this.citizenName)) {
                return false;
            }

            int amount = this.countItems(player);
            return amount > 0;
        }

        return false;
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.INVENTORY_CONTENT;
    }

    private boolean isItemStack(ItemStack itemStack) {
        if (itemStack == null) return false;

        if (this.material != null && itemStack.getType() != this.material) return false;
        if (this.materialTag != null && !this.materialTag.isTagged(itemStack.getType())) return false;

        if (this.needItemMeta()) {

            if (!itemStack.hasItemMeta()) return false;

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.getCustomModelData() != this.customModelId) return false;
        }
        return true;
    }

    public int countItems(Player player) {
        int count = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (!isItemStack(itemStack)) continue;
            count += itemStack.getAmount();
        }
        return count;
    }

    public void removeItems(Player player, int amountToRemove) {
        if (amountToRemove <= 0) return;

        int removed = 0;
        PlayerInventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null || itemStack.getType().isAir()) continue;

            int itemAmount = itemStack.getAmount();
            int remove = Math.min(itemAmount, amountToRemove - removed);

            if (itemAmount - remove <= 0) {
                inventory.setItem(i, null);
            } else {
                itemStack.setAmount(itemAmount - remove);
            }

            removed += remove;
            if (removed >= amountToRemove) break;
        }
    }

}
