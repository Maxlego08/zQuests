package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;

public class CommandQuestsReward extends VCommand {

    public CommandQuestsReward(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_RESTART);
        this.addSubCommand("reward");
        this.setDescription(Message.DESCRIPTION_RESTART);
        this.addRequireArg("player");
        this.addRequireArg("quest", (sender, args) -> plugin.getQuestManager().getQuests().stream().map(Quest::getName).toList());
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        var player = this.argAsPlayer(0);
        String questName = this.argAsString(1);
        plugin.getQuestManager().giveQuestReward(sender, player, questName);

        return CommandType.SUCCESS;
    }

}
