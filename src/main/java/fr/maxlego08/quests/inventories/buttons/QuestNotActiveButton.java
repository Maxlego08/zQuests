package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.UserQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class QuestNotActiveButton extends Button {

    private final QuestsPlugin plugin;
    private final List<Quest> quests;

    public QuestNotActiveButton(QuestsPlugin plugin, List<Quest> quests) {
        this.plugin = plugin;
        this.quests = quests;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {

        QuestManager questManager = this.plugin.getQuestManager();
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());

        boolean hasNotQuestActive = quests.stream().noneMatch(userQuest::isQuestActive);

        return super.checkPermission(player, inventory, placeholders) && hasNotQuestActive;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {

        if (!checkPermission(player, inventory, placeholders)) {
            return;
        }

        super.onClick(player, event, inventory, slot, placeholders);
    }
}
