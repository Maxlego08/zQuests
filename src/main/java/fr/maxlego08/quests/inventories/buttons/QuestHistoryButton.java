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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuestHistoryButton extends ZButton implements PaginateButton {

    private final QuestsPlugin plugin;
    private final List<Integer> offsetSlots;
    private final MenuItemStack completedItem;
    private final boolean enableCompleted;
    private final QuestManager manager;

    public QuestHistoryButton(QuestsPlugin plugin, List<Integer> offsetSlots, MenuItemStack completedItem, boolean enableCompleted) {
        this.plugin = plugin;
        this.offsetSlots = offsetSlots;
        this.completedItem = completedItem;
        this.enableCompleted = enableCompleted;
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

        quests = quests.stream()
                .sorted(Comparator.comparingInt(QuestHistory::sortActive))
                .sorted(Comparator.comparingInt(QuestHistory::sortFav).reversed())
                .sorted(Comparator.comparingInt(QuestHistory::sortCompletedDate))
                .toList();

        this.paginate(quests, inventory, (slot, questHistory) -> {

            var menuItemStack = questHistory.activeQuest != null ? this.getItemStack() : this.completedItem;
            var quest = questHistory.getQuest();
            if (quest == null) {
                this.plugin.getLogger().info("Quest is null ! " + questHistory);
                return;
            }

            Placeholders placeholders = new Placeholders();
            placeholders.register("quest-name", quest.getName());
            placeholders.register("quest-description", quest.getDescription());
            placeholders.register("quest-thumbnail", quest.getThumbnail().name());
            placeholders.register("quest-type", quest.getType().name());
            placeholders.register("quest-objective", String.valueOf(quest.getGoal()));
            placeholders.register("quest-lore-line", this.plugin.getQuestPlaceholder().getLoreLine(player, quest.getName()));
            placeholders.register("quest-progress-bar", this.plugin.getQuestPlaceholder().getProgressBar(player, quest.getName()));
            placeholders.register("quest-percent", this.plugin.getQuestPlaceholder().getPercent(player, quest.getName()));
            placeholders.register("quest-progress", String.valueOf(this.plugin.getQuestPlaceholder().getProgress(player, quest.getName())));

            for (Integer offsetSlot : this.offsetSlots) {

                inventory.addItem(slot + offsetSlot, menuItemStack.build(player, false, placeholders)).setClick(e -> {

                });
            }
        });
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
    }
}
