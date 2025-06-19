package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangePlaceholderTypeEvent;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SetFavoriteTypeButton extends Button {

    private final QuestsPlugin plugin;
    private final FavoritePlaceholderType favoritePlaceholderType;

    public SetFavoriteTypeButton(QuestsPlugin plugin, FavoritePlaceholderType favoritePlaceholderType) {
        this.plugin = plugin;
        this.favoritePlaceholderType = favoritePlaceholderType;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        return super.checkPermission(player, inventory, placeholders) && (this.plugin.getQuestManager().getUserQuest(player.getUniqueId()).getFavoritePlaceholderType() != this.favoritePlaceholderType);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);
        var manager = this.plugin.getQuestManager();
        var userQuest = manager.getUserQuest(player.getUniqueId());

        var typeEvent = new QuestFavoriteChangePlaceholderTypeEvent(userQuest, this.favoritePlaceholderType);
        if (manager.callQuestEvent(player.getUniqueId(), typeEvent)) return;

        userQuest.setFavoritePlaceholderType(typeEvent.getNewFavoritePlaceholderType());
        this.plugin.getStorageManager().upsertPlayerFavoriteQuestConfiguration(player.getUniqueId(), userQuest.getFavoriteLimit(), typeEvent.getNewFavoritePlaceholderType());
        this.plugin.getInventoryManager().updateInventory(player);
    }
}
