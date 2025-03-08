package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.event.events.QuestFavoriteChangeEvent;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.zcore.utils.QuestPlaceholderUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

        var userQuests = this.manager.getUserQuest(player.getUniqueId());
        List<QuestHistory> quests = new ArrayList<>();
        quests.addAll(userQuests.getActiveQuests().stream().map(e -> new QuestHistory(e, null)).toList());
        quests.addAll(userQuests.getCompletedQuests().stream().map(e -> new QuestHistory(null, e)).toList());

        quests = quests.stream().sorted(Comparator.comparingInt(QuestHistory::sortActive)).sorted(Comparator.comparingInt(QuestHistory::sortFav).reversed()).sorted(Comparator.comparingInt(QuestHistory::sortCompletedDate)).toList();

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
        var menuItemStack = questHistory.isActive() ? questHistory.activeQuest.isFavorite() ? this.favConfiguration.enable : this.favConfiguration.disable : this.favConfiguration.completed;

        placeholders.register("quest-lore", quest.canChangeFavorite() && questHistory.isActive() ? questHistory.activeQuest.isFavorite() ? this.favConfiguration.loreEnable : this.favConfiguration.loreDisable : this.favConfiguration.loreCancel);

        inventory.addItem(slot + this.favConfiguration.offset, menuItemStack.build(player, false, placeholders)).setClick(e -> {

            if (!questHistory.isActive()) return;
            if (!quest.canChangeFavorite()) return;

            var activeQuest = questHistory.activeQuest;
            QuestFavoriteChangeEvent event = new QuestFavoriteChangeEvent(player, activeQuest, !activeQuest.isFavorite());
            if (this.plugin.getQuestManager().callQuestEvent(player.getUniqueId(), event)) return;

            activeQuest.setFavorite(event.isFavorite());
            this.plugin.getStorageManager().upsert(activeQuest);

            this.plugin.getInventoryManager().updateInventory(player);
        });
    }

    private Placeholders createPlaceholder(Quest quest, Player player, QuestHistory questHistory) {
        Placeholders placeholders = QuestPlaceholderUtil.createPlaceholder(this.plugin, player, quest);

        placeholders.register("quest-started-at", questHistory.getStartedAt());
        placeholders.register("quest-finished-at", questHistory.getFinishedAt());

        return placeholders;
    }

    @Override
    public int getPaginationSize(Player player) {
        var userQuests = this.manager.getUserQuest(player.getUniqueId());
        return userQuests.getActiveQuests().size() + (this.enableCompleted ? userQuests.getCompletedQuests().size() : 0);
    }

    public record QuestHistory(ActiveQuest activeQuest, CompletedQuest completedQuest) {

        public int sortFav() {
            return this.activeQuest != null && this.activeQuest.isFavorite() ? 1 : 0;
        }

        public int sortActive() {
            return this.activeQuest != null ? 1 : 0;
        }

        public int sortCompletedDate() {
            return this.completedQuest != null ? (int) this.completedQuest.completedAt().getTime() : Integer.MIN_VALUE;
        }

        public Quest getQuest() {
            return this.activeQuest != null ? this.activeQuest.getQuest() : this.completedQuest.quest();
        }

        public boolean isActive() {
            return this.activeQuest != null;
        }

        public String getStartedAt() {
            return Config.simpleDateFormat.format(this.isActive() ? this.activeQuest.getCreatedAt() : this.completedQuest.startedAt());
        }

        public String getFinishedAt() {
            return Config.simpleDateFormat.format(this.isActive() ? new Date() : this.completedQuest.completedAt());
        }
    }

    public record FavConfiguration(int offset, MenuItemStack enable, MenuItemStack disable, MenuItemStack completed,
                                   String loreEnable, String loreDisable, String loreCancel) {

    }
}
