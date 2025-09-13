package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandQuestsRefreshHologram extends VCommand {

    public CommandQuestsRefreshHologram(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_REFRESH_HOLOGRAM);
        this.addSubCommand("refresh-hologram");
        this.setDescription(Message.DESCRIPTION_REFRESH_HOLOGRAM);
        this.addRequireArg("player");
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        plugin.getHologramManager().refreshHologram(player);
        message(sender, Message.HOLOGRAM_REFRESH, "%player%", player.getName());

        return CommandType.SUCCESS;
    }

}
