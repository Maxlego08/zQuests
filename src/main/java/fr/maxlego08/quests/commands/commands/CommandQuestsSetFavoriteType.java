package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;

public class CommandQuestsSetFavoriteType extends VCommand {

    public CommandQuestsSetFavoriteType(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_SET_FAVORITE_LIMIT);
        this.addSubCommand("set-favorite-type");
        this.setDescription(Message.DESCRIPTION_SET_FAVORITE_TYPE);
        this.addRequireArg("player");
        this.addRequireArg("type", (sender, args) -> Arrays.stream(FavoritePlaceholderType.values()).map(Enum::name).toList());
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        FavoritePlaceholderType favoritePlaceholderType = FavoritePlaceholderType.valueOf(this.argAsString(1).toUpperCase());
        plugin.getQuestManager().setFavoriteType(sender, offlinePlayer, favoritePlaceholderType);

        return CommandType.SUCCESS;
    }

}
