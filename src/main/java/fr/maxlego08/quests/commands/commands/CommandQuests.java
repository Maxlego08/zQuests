package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.utils.QuestInventoryPage;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class CommandQuests extends VCommand {

    public CommandQuests(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_USE);
        this.addSubCommand(new CommandQuestsReload(plugin));
        this.addSubCommand(new CommandQuestsStart(plugin));
        this.addSubCommand(new CommandQuestsStartAll(plugin));
        this.addSubCommand(new CommandQuestsDelete(plugin));
        this.addSubCommand(new CommandQuestsDeleteAll(plugin));
        this.addSubCommand(new CommandQuestsComplete(plugin));
        this.addSubCommand(new CommandQuestsCompleteAll(plugin));
        this.addSubCommand(new CommandQuestsAddProgress(plugin));
        this.addSubCommand(new CommandQuestsSetProgress(plugin));
        this.addSubCommand(new CommandQuestsHelp(plugin));
        this.addSubCommand(new CommandQuestsProgressInventory(plugin));
        this.addSubCommand(new CommandQuestsSetFavorite(plugin));
        this.addSubCommand(new CommandQuestsRestart(plugin));
        this.addSubCommand(new CommandQuestsRefreshHologram(plugin));
        this.addSubCommand(new CommandQuestsAddFavorite(plugin));
        this.addSubCommand(new CommandQuestsShow(plugin));
        this.addSubCommand(new CommandQuestsSetFavoriteLimit(plugin));
        this.addSubCommand(new CommandQuestsSetFavoriteType(plugin));
        this.addSubCommand(new CommandQuestsReward(plugin));
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        if (sender instanceof Player currentPlayer) {

            var mainPage = new QuestInventoryPage("", Config.mainCommandInventoryName, 1, 0);

            var result = Config.enableQuestInventoryPages ? Config.questInventoryPages.stream()
                    .filter(questInventoryPage -> currentPlayer.hasPermission(questInventoryPage.permission()))
                    .max(Comparator.comparingInt(QuestInventoryPage::priority))
                    .orElse(mainPage) : mainPage;

            plugin.getQuestManager().openQuestInventory(currentPlayer, result);
        } else syntaxMessage(this.sender);

        return CommandType.SUCCESS;
    }

}
