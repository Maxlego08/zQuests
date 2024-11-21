package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;

public class CommandQuests extends VCommand {

	public CommandQuests(QuestsPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZQUESTS_USE);
		this.addSubCommand(new CommandQuestsReload(plugin));
		this.addSubCommand(new CommandQuestsStart(plugin));
		this.addSubCommand(new CommandQuestsDeleteAll(plugin));
		this.addSubCommand(new CommandQuestsDelete(plugin));
		this.addSubCommand(new CommandQuestsComplete(plugin));
		this.addSubCommand(new CommandQuestsSetProgress(plugin));
	}

	@Override
	protected CommandType perform(QuestsPlugin plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}
