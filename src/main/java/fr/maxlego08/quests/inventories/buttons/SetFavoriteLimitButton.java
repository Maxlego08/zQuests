package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeLimitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SetFavoriteLimitButton extends ZButton {

    private final QuestsPlugin plugin;
    private final int limit;

    public SetFavoriteLimitButton(QuestsPlugin plugin, int limit) {
        this.plugin = plugin;
        this.limit = limit;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryDefault inventory, Placeholders placeholders) {
        return super.checkPermission(player, inventory, placeholders) && (this.plugin.getQuestManager().getUserQuest(player.getUniqueId()).getFavoriteLimit() == this.limit);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);
        var manager = this.plugin.getQuestManager();
        var userQuest = manager.getUserQuest(player.getUniqueId());

        QuestFavoriteChangeLimitEvent amountEvent = new QuestFavoriteChangeLimitEvent(userQuest, this.limit);
        if (manager.callQuestEvent(player.getUniqueId(), amountEvent)) return;

        userQuest.setFavoriteLimit(amountEvent.getNewLimit());
        this.plugin.getStorageManager().upsertPlayerFavoriteQuestConfiguration(player.getUniqueId(), amountEvent.getNewLimit(), userQuest.getFavoritePlaceholderType());
        this.plugin.getInventoryManager().updateInventory(player);
    }
}
