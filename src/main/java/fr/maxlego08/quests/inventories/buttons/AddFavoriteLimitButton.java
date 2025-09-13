package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeLimitEvent;
import fr.maxlego08.quests.save.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import java.awt.*;

public class AddFavoriteLimitButton extends Button {

    private final QuestsPlugin plugin;

    public AddFavoriteLimitButton(Plugin plugin) {
        this.plugin = (QuestsPlugin) plugin;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {

        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        var placeholderFavorite = Config.placeholderFavorites.get(userQuest.getFavoritePlaceholderType());

        return super.checkPermission(player, inventory, placeholders) && this.plugin.getQuestManager().getUserQuest(player.getUniqueId()).getFavoriteLimit() < placeholderFavorite.maxFavorite();
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);
        var manager = this.plugin.getQuestManager();
        var userQuest = manager.getUserQuest(player.getUniqueId());
        var placeholderFavorite = Config.placeholderFavorites.get(userQuest.getFavoritePlaceholderType());

        int newLimit = userQuest.getFavoriteLimit() + 1;
        if (newLimit > placeholderFavorite.maxFavorite()) newLimit = placeholderFavorite.maxFavorite();

        QuestFavoriteChangeLimitEvent amountEvent = new QuestFavoriteChangeLimitEvent(userQuest, newLimit);
        if (manager.callQuestEvent(player.getUniqueId(), amountEvent)) return;

        userQuest.setFavoriteLimit(amountEvent.getNewLimit());
        this.plugin.getStorageManager().upsertPlayerFavoriteQuestConfiguration(player.getUniqueId(), amountEvent.getNewLimit(), userQuest.getFavoritePlaceholderType());
        this.plugin.getInventoryManager().updateInventory(player);
    }
}
