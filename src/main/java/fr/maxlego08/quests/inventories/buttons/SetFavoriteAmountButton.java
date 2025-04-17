package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeAmountEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SetFavoriteAmountButton extends ZButton {

    private final QuestsPlugin plugin;
    private final int amount;

    public SetFavoriteAmountButton(QuestsPlugin plugin, int amount) {
        this.plugin = plugin;
        this.amount = amount;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryDefault inventory, Placeholders placeholders) {
        return super.checkPermission(player, inventory, placeholders) && (this.plugin.getQuestManager().getUserQuest(player.getUniqueId()).getFavoriteAmount() == this.amount);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);
        var manager = this.plugin.getQuestManager();
        var userQuest = manager.getUserQuest(player.getUniqueId());

        QuestFavoriteChangeAmountEvent amountEvent = new QuestFavoriteChangeAmountEvent(userQuest, this.amount);
        if (manager.callQuestEvent(player.getUniqueId(), amountEvent)) return;

        userQuest.setFavoriteAmount(amountEvent.getNewAmount());
        this.plugin.getStorageManager().upsertPlayerFavoriteQuestAmount(player.getUniqueId(), amountEvent.getNewAmount());
        this.plugin.getInventoryManager().updateInventory(player);
    }
}
