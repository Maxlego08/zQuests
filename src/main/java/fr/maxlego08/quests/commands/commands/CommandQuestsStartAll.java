package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandQuestsStartAll extends VCommand {

    public CommandQuestsStartAll(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_START_ALL);
        this.addSubCommand("start-all");
        this.setDescription(Message.DESCRIPTION_START_ALL);
        this.addRequireArg("player");
        this.addRequireArg("quest", (sender, args) -> plugin.getQuestManager().getGroup().keySet().stream().toList());
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String groupName = this.argAsString(1);
        plugin.getQuestManager().activateQuestGroup(sender, player, groupName);

        return CommandType.SUCCESS;
    }

}
