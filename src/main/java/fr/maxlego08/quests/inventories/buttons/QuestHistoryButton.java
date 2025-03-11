package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeEvent;
import fr.maxlego08.quests.api.utils.QuestHistory;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.zcore.utils.QuestPlaceholderUtil;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class QuestHistoryButton extends ZButton implements PaginateButton {

    private final QuestsPlugin plugin;
    private final List<Integer> offsetSlots;
    private final MenuItemStack completedItem;
    private final boolean enableCompleted;
    private final int offsetCustomModelId;
    private final FavConfiguration favConfiguration;
    private final QuestManager manager;

    public QuestHistoryButton(QuestsPlugin plugin, List<Integer> offsetSlots, MenuItemStack completedItem, boolean enableCompleted, int offsetCustomModelId, FavConfiguration favConfiguration) {
        this.plugin = plugin;
        this.offsetSlots = offsetSlots;
        this.completedItem = completedItem;
        this.enableCompleted = enableCompleted;
        this.offsetCustomModelId = offsetCustomModelId;
        this.favConfiguration = favConfiguration;
        this.manager = this.plugin.getQuestManager();
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public boolean isPermanent() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {

        var quests = this.manager.getDisplayQuests(player);

        this.paginate(quests, inventory, (slot, questHistory) -> {

            var menuItemStack = questHistory.isActive() ? this.getItemStack() : this.completedItem;

            var quest = questHistory.getQuest();
            if (quest == null) {
                this.plugin.getLogger().info("Quest is null ! " + questHistory);
                return;
            }

            Placeholders placeholders = createPlaceholder(quest, player, questHistory);

            for (Integer offsetSlot : this.offsetSlots) {

                placeholders.register("quest-model-id", String.valueOf(offsetSlot == 0 ? quest.getCustomModelId() : this.offsetCustomModelId));

                inventory.addItem(slot + offsetSlot, menuItemStack.build(player, false, placeholders)).setClick(e -> {

                });
            }

            // Fav Configuration
            this.displayFav(player, questHistory, inventory, slot, placeholders);
        });
    }

    private void displayFav(Player player, QuestHistory questHistory, InventoryDefault inventory, int slot, Placeholders placeholders) {

        var quest = questHistory.getQuest();
        var menuItemStack = questHistory.isActive() ? questHistory.activeQuest().isFavorite() ? this.favConfiguration.enable : this.favConfiguration.disable : this.favConfiguration.completed;

        placeholders.register("quest-lore", quest.canChangeFavorite() && questHistory.isActive() ? questHistory.activeQuest().isFavorite() ? this.favConfiguration.loreEnable : this.favConfiguration.loreDisable : this.favConfiguration.loreCancel);

        inventory.addItem(slot + this.favConfiguration.offset, menuItemStack.build(player, false, placeholders)).setClick(e -> {

            if (!questHistory.isActive()) return;
            if (!quest.canChangeFavorite()) return;

            var activeQuest = questHistory.activeQuest();
            QuestFavoriteChangeEvent event = new QuestFavoriteChangeEvent(player, activeQuest, !activeQuest.isFavorite());
            if (this.plugin.getQuestManager().callQuestEvent(player.getUniqueId(), event)) return;

            activeQuest.setFavorite(event.isFavorite());
            this.plugin.getStorageManager().upsert(activeQuest);

            this.plugin.getInventoryManager().updateInventory(player);
        });
    }

    private Placeholders createPlaceholder(Quest quest, Player player, QuestHistory questHistory) {
        Placeholders placeholders = QuestPlaceholderUtil.createPlaceholder(this.plugin, player, quest);

        placeholders.register("quest-started-at", questHistory.getStartedAt(Config.simpleDateFormat));
        placeholders.register("quest-finished-at", questHistory.getFinishedAt(Config.simpleDateFormat));

        return placeholders;
    }

    @Override
    public int getPaginationSize(Player player) {
        return this.manager.getDisplayQuests(player).size();
    }

    public record FavConfiguration(int offset, MenuItemStack enable, MenuItemStack disable, MenuItemStack completed,
                                   String loreEnable, String loreDisable, String loreCancel) {

    }
}
