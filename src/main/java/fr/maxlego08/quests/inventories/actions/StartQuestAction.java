package fr.maxlego08.quests.inventories.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.UserQuest;
import org.bukkit.entity.Player;

import java.util.List;

public class StartQuestAction extends Action {

    private final QuestsPlugin plugin;
    private final List<String> quests;

    public StartQuestAction(QuestsPlugin plugin, List<String> quests) {
        this.plugin = plugin;
        this.quests = quests;
    }

    @Override
    protected void execute(Player player, Button button, InventoryDefault inventoryDefault, Placeholders placeholders) {
        var manager = this.plugin.getQuestManager();
        for (String questName : quests) {
            var optional = manager.getQuest(questName);
            if (optional.isPresent()) {
                var quest = optional.get();
                UserQuest userQuest = manager.getUserQuest(player.getUniqueId());
                if (userQuest.canStartQuest(quest)) {
                    manager.addQuestToPlayer(player.getUniqueId(), quest);
                }
            }
        }
    }
}
