package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangePlaceholderTypeEvent;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.quests.save.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class ChangeFavoriteTypeButton extends ZButton {

    private final QuestsPlugin plugin;

    public ChangeFavoriteTypeButton(Plugin plugin) {
        this.plugin = (QuestsPlugin) plugin;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);
        var manager = this.plugin.getQuestManager();
        var userQuest = manager.getUserQuest(player.getUniqueId());

        var currentPlaceholderType = userQuest.getFavoritePlaceholderType();
        FavoritePlaceholderType[] types = FavoritePlaceholderType.values();
        int index = (currentPlaceholderType.ordinal() + 1) % types.length;
        currentPlaceholderType = types[index];

        var typeEvent = new QuestFavoriteChangePlaceholderTypeEvent(userQuest, currentPlaceholderType);
        if (manager.callQuestEvent(player.getUniqueId(), typeEvent)) return;

        var placeholderFavorite = Config.placeholderFavorites.get(typeEvent.getNewFavoritePlaceholderType());
        int limit = userQuest.getFavoriteLimit();
        if (limit < placeholderFavorite.minFavorite()) {
            limit = placeholderFavorite.minFavorite();
        } else if (limit > placeholderFavorite.maxFavorite()) {
            limit = placeholderFavorite.maxFavorite();
        }

        userQuest.setFavoriteLimit(limit);
        userQuest.setFavoritePlaceholderType(typeEvent.getNewFavoritePlaceholderType());
        this.plugin.getStorageManager().upsertPlayerFavoriteQuestConfiguration(player.getUniqueId(), limit, typeEvent.getNewFavoritePlaceholderType());
        this.plugin.getInventoryManager().updateInventory(player);
    }
}
