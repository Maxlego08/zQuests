package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandQuestsStart extends VCommand {

    public CommandQuestsStart(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_START);
        this.addSubCommand("start");
        this.setDescription(Message.DESCRIPTION_START);
        this.addRequireArg("player");
        this.addRequireArg("quest", (sender, args) -> {

            var quests = plugin.getQuestManager().getQuests();

            if (args.length <= 1) return quests.stream().map(Quest::getName).toList();

            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) return quests.stream().map(Quest::getName).toList();

            var userQuest = plugin.getQuestManager().getUserQuest(player.getUniqueId());
            return quests.stream().filter(userQuest::canStartQuest).map(Quest::getName).toList();
        });
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String questName = this.argAsString(1);
        plugin.getQuestManager().activateQuest(sender, player, questName);

        return CommandType.SUCCESS;
    }

}
