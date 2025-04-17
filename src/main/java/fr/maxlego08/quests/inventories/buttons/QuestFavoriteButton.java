package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class QuestFavoriteButton extends ZButton {

    private final QuestsPlugin plugin;
    private final List<String> quests;
    private final String actionEnable;
    private final String actionDisable;

    public QuestFavoriteButton(QuestsPlugin plugin, List<String> quests, String actionEnable, String actionDisable) {
        this.plugin = plugin;
        this.quests = quests;
        this.actionEnable = actionEnable;
        this.actionDisable = actionDisable;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        Placeholders placeholders = new Placeholders();

        var manager = this.plugin.getQuestManager();
        var userQuest = manager.getUserQuest(player.getUniqueId());
        var quests = userQuest.getActiveQuests().stream().filter(activeQuest -> this.quests.contains(activeQuest.getQuest().getName())).toList();
        var favQuests = quests.stream().filter(ActiveQuest::isFavorite).count();

        placeholders.register("action", favQuests >= quests.size() ? this.actionDisable : this.actionEnable);

        return getItemStack().build(player, false, placeholders);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        var manager = this.plugin.getQuestManager();
        var userQuest = manager.getUserQuest(player.getUniqueId());
        var quests = userQuest.getActiveQuests().stream().filter(activeQuest -> this.quests.contains(activeQuest.getQuest().getName())).toList();
        var favQuests = quests.stream().filter(ActiveQuest::isFavorite).count();

        var newValue = favQuests < quests.size();

        for (ActiveQuest activeQuest : quests) {

            QuestFavoriteChangeEvent changeEvent = new QuestFavoriteChangeEvent(player, activeQuest, newValue);
            if (this.plugin.getQuestManager().callQuestEvent(player.getUniqueId(), changeEvent)) continue;

            activeQuest.setFavorite(changeEvent.isFavorite());
            this.plugin.getStorageManager().upsert(activeQuest);
        }

        this.plugin.getInventoryManager().updateInventory(player);
    }
}
