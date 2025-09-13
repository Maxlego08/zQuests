package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandQuestsComplete extends VCommand {

    public CommandQuestsComplete(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_COMPLETE);
        this.addSubCommand("complete");
        this.setDescription(Message.DESCRIPTION_COMPLETE);
        this.addRequireArg("player");
        this.addRequireArg("quest", (sender, args) -> {

            if (args.length <= 1) return List.of();

            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) return List.of();

            var userQuest = plugin.getQuestManager().getUserQuest(player.getUniqueId());
            return userQuest.getActiveQuests().stream().map(activeQuest -> activeQuest.getQuest().getName()).toList();
        });
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String questName = this.argAsString(1);
        plugin.getQuestManager().completeQuest(sender, player, questName);

        return CommandType.SUCCESS;
    }

}
