package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

public class CommandQuestsDeleteAll extends VCommand {

    public CommandQuestsDeleteAll(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_DELETE_ALL);
        this.addSubCommand("delete-all");
        this.setDescription(Message.DESCRIPTION_DELETE_ALL);
        this.addRequireArg("player");
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        plugin.getQuestManager().deleteUserQuests(sender, offlinePlayer);

        return CommandType.SUCCESS;
    }

}
