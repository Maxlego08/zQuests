package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.UserQuest;
import org.bukkit.entity.Player;

import java.util.List;

public class QuestCompleteButton extends ZButton {

    private final QuestsPlugin plugin;
    private final List<Quest> quests;

    public QuestCompleteButton(QuestsPlugin plugin, List<Quest> quests) {
        this.plugin = plugin;
        this.quests = quests;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryDefault inventory, Placeholders placeholders) {

        QuestManager questManager = this.plugin.getQuestManager();
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());

        boolean hasCompleted = quests.stream().allMatch(userQuest::isQuestCompleted);

        return super.checkPermission(player, inventory, placeholders) && hasCompleted;
    }
}
