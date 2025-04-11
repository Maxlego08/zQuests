package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.ZPlugin;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;

public class CommandQuestsReload extends VCommand {

    public CommandQuestsReload(ZPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_RELOAD);
        this.addSubCommand("reload", "rl");
        this.setDescription(Message.DESCRIPTION_RELOAD);
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        plugin.reloadFiles();
        Bukkit.getOnlinePlayers().forEach(e -> plugin.getHologramManager().refreshHologram(e));
        message(sender, Message.RELOAD);

        return CommandType.SUCCESS;
    }

}
