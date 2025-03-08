package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.utils.InventoryContent;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandQuestsProgressInventory extends VCommand {

    public CommandQuestsProgressInventory(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_PROGRESS_INVENTORY);
        this.addSubCommand("progress-inventory");
        this.setDescription(Message.DESCRIPTION_PROGRESS_INVENTORY);
        this.addRequireArg("player");
        this.addOptionalArg("citizen name");
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String citizenName = this.argAsString(1, null);

        int amount = plugin.getQuestManager().handleInventoryQuests(new InventoryContent(player, citizenName));
        message(player, Message.PROGRESS_INVENTORY_INFO, "%player%", player.getName(), "%amount%", amount);

        return CommandType.SUCCESS;
    }

}
