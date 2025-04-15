package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandQuestsAddFavorite extends VCommand {

    public CommandQuestsAddFavorite(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_ADD_FAVORITE);
        this.addSubCommand("add-favorite");
        this.setDescription(Message.DESCRIPTION_ADD_FAVORITE);
        this.addRequireArg("quest");
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        String questName = this.argAsString(0);
        plugin.getQuestManager().setFavorite(Bukkit.getConsoleSender(), this.player, questName, true);

        return CommandType.SUCCESS;
    }

}
