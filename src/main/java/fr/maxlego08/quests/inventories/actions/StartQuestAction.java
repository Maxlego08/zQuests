package fr.maxlego08.quests.inventories.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.UserQuest;
import org.bukkit.entity.Player;

public class StartQuestAction extends Action {

    private final QuestsPlugin plugin;
    private final String questName;

    public StartQuestAction(QuestsPlugin plugin, String questName) {
        this.plugin = plugin;
        this.questName = questName;
    }

    @Override
    protected void execute(Player player, Button button, InventoryDefault inventoryDefault, Placeholders placeholders) {
        var manager = this.plugin.getQuestManager();
        var optional = manager.getQuest(this.questName);
        if (optional.isPresent()) {
            var quest = optional.get();
            UserQuest userQuest = manager.getUserQuest(player.getUniqueId());
            if (userQuest.canStartQuest(quest)) {
                manager.addQuestToPlayer(player.getUniqueId(), quest);
            }
        }
    }
}
