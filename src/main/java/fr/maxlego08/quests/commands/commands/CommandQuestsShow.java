package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandQuestsShow extends VCommand {

    public CommandQuestsShow(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_SHOW);
        this.addSubCommand("show");
        this.setDescription(Message.DESCRIPTION_SHOW);
        this.addRequireArg("player");
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        plugin.getQuestManager().showQuests(sender, offlinePlayer);

        return CommandType.SUCCESS;
    }

}
