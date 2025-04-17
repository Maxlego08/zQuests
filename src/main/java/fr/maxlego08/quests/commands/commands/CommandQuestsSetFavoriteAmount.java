package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandQuestsSetFavoriteAmount extends VCommand {

    public CommandQuestsSetFavoriteAmount(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_SET_FAVORITE_AMOUNT);
        this.addSubCommand("set-favorite-amount");
        this.setDescription(Message.DESCRIPTION_SET_FAVORITE_AMOUNT);
        this.addRequireArg("player");
        this.addRequireArg("amount", (sender, args) -> Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        int amount = this.argAsInteger(1);
        plugin.getQuestManager().setFavoriteAmount(sender, offlinePlayer, amount);

        return CommandType.SUCCESS;
    }

}
