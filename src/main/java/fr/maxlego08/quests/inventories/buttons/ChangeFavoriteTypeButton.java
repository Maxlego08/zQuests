package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangePlaceholderTypeEvent;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
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

        var current = userQuest.getFavoritePlaceholderType();
        FavoritePlaceholderType[] types = FavoritePlaceholderType.values();
        int index = (current.ordinal() + 1) % types.length;
        current = types[index];

        var typeEvent = new QuestFavoriteChangePlaceholderTypeEvent(userQuest, current);
        if (manager.callQuestEvent(player.getUniqueId(), typeEvent)) return;

        userQuest.setFavoritePlaceholderType(typeEvent.getNewFavoritePlaceholderType());
        this.plugin.getStorageManager().upsertPlayerFavoriteQuestConfiguration(player.getUniqueId(), userQuest.getFavoriteLimit(), typeEvent.getNewFavoritePlaceholderType());
        this.plugin.getInventoryManager().updateInventory(player);
    }
}
