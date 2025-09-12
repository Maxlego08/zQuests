package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandQuestsHelp extends VCommand {

    public CommandQuestsHelp(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_HELP);
        this.addSubCommand("help", "?");
        this.setDescription(Message.DESCRIPTION_HELP);
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        this.parent.syntaxMessage(this.sender);

        return CommandType.SUCCESS;
    }

}
