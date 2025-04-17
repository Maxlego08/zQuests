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
    private final List<String> questNames;
    private final String actionEnable;
    private final String actionDisable;

    public QuestFavoriteButton(QuestsPlugin plugin, List<String> questNames, String actionEnable, String actionDisable) {
        this.plugin = plugin;
        this.questNames = questNames;
        this.actionEnable = actionEnable;
        this.actionDisable = actionDisable;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        Placeholders placeholders = new Placeholders();

        List<ActiveQuest> relevantQuests = getRelevantActiveQuests(player);
        boolean isFavorite = relevantQuests.stream().allMatch(ActiveQuest::isFavorite);

        String actionText = isFavorite ? actionDisable : actionEnable;
        placeholders.register("action", actionText);

        return getItemStack().build(player, false, placeholders);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        List<ActiveQuest> relevantQuests = getRelevantActiveQuests(player);
        long favoriteCount = relevantQuests.stream().filter(ActiveQuest::isFavorite).count();
        boolean newFavoriteValue = !relevantQuests.stream().allMatch(ActiveQuest::isFavorite);

        for (ActiveQuest activeQuest : relevantQuests) {
            QuestFavoriteChangeEvent changeEvent = new QuestFavoriteChangeEvent(player, activeQuest, newFavoriteValue);
            if (plugin.getQuestManager().callQuestEvent(player.getUniqueId(), changeEvent)) continue;

            activeQuest.setFavorite(changeEvent.isFavorite());
            plugin.getStorageManager().upsert(activeQuest);
        }

        plugin.getInventoryManager().updateInventory(player);
    }

    private List<ActiveQuest> getRelevantActiveQuests(Player player) {
        return plugin.getQuestManager().getUserQuest(player.getUniqueId()).getActiveQuests().stream().filter(aq -> questNames.contains(aq.getQuest().getName())).toList();
    }
}
