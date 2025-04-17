package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandQuestsSetFavorite extends VCommand {

    public CommandQuestsSetFavorite(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_SET_FAVORITE);
        this.addSubCommand("set-favorite");
        this.setDescription(Message.DESCRIPTION_SET_FAVORITE);
        this.addRequireArg("player");
        this.addRequireArg("quest", (sender, args) -> {

            if (args.length <= 1) return List.of();

            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) return List.of();

            var userQuest = plugin.getQuestManager().getUserQuest(player.getUniqueId());
            return userQuest.getActiveQuests().stream().map(activeQuest -> activeQuest.getQuest().getName()).toList();
        });
        this.addRequireArg("boolean", (a, b) -> List.of("true", "false"));
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String questName = this.argAsString(1);
        boolean amount = this.argAsBoolean(2);
        plugin.getQuestManager().setFavorite(sender, player, questName, amount);

        return CommandType.SUCCESS;
    }

}
