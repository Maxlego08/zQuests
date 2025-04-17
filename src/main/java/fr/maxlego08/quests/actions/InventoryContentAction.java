package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.InventoryContent;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryContentAction extends ZUtils implements QuestAction {

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

    private boolean isNotItemStack(ItemStack itemStack) {
        if (itemStack == null) return true;

        if (this.material != null && itemStack.getType() != this.material) return true;
        if (this.materialTag != null && !this.materialTag.isTagged(itemStack.getType())) return true;

        if (this.needItemMeta()) {

            if (!itemStack.hasItemMeta()) return true;

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.getCustomModelData() != this.customModelId) return true;
        }
        return false;
    }

    public int countItems(Player player) {
        int count = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (isNotItemStack(itemStack)) continue;
            count += itemStack.getAmount();
        }
        return count;
    }

    public void removeItems(Player player, int amountToRemove) {
        if (amountToRemove <= 0) return;

        int removed = 0;
        PlayerInventory inventory = player.getInventory();
        Map<Material, Integer> map = new HashMap<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null || itemStack.getType().isAir() || isNotItemStack(itemStack)) continue;

            int itemAmount = itemStack.getAmount();
            int remove = Math.min(itemAmount, amountToRemove - removed);

            if (itemAmount - remove <= 0) {
                inventory.setItem(i, null);
            } else {
                itemStack.setAmount(itemAmount - remove);
            }

            map.put(itemStack.getType(), map.getOrDefault(itemStack.getType(), 0) + remove);
            removed += remove;
            if (removed >= amountToRemove) break;
        }

        String items = map.entrySet().stream().map(entry -> getMessage(Message.INVENTORY_CONTENT, "%limit%", entry.getValue(), "%material-key%", entry.getKey().translationKey(), "%material%", name(entry.getKey()))).collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i < list.size() - 1) {
                    sb.append(i < list.size() - 2 ? ", " : getMessage(Message.INVENTORY_AND));
                }
            }
            return sb.toString();
        }));

        message(player, Message.INVENTORY_REMOVE, "%items%", items);
    }

}
