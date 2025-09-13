package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.ZPlugin;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;

public class CommandQuestsReloadInventories extends VCommand {

    public CommandQuestsReloadInventories(ZPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_RELOAD_INVENTORIES);
        this.addSubCommand("inventories");
        this.setDescription(Message.DESCRIPTION_RELOAD_INVENTORIES);
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        plugin.getQuestManager().loadPatterns();
        plugin.getQuestManager().loadInventories();
        message(sender, Message.RELOAD_INVENTORIES);

        return CommandType.SUCCESS;
    }

}
