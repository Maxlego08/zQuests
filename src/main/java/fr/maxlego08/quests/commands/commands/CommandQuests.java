package fr.maxlego08.quests.commands.commands;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.utils.QuestInventoryPage;
import fr.maxlego08.quests.commands.VCommand;
import fr.maxlego08.quests.zcore.enums.Permission;
import fr.maxlego08.quests.zcore.utils.commands.CommandType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CommandQuests extends VCommand {

    private final List<QuestInventoryPage> questInventoryPages = new ArrayList<>();

    public CommandQuests(QuestsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZQUESTS_USE);
        this.addSubCommand(new CommandQuestsReload(plugin));
        this.addSubCommand(new CommandQuestsStart(plugin));
        this.addSubCommand(new CommandQuestsDeleteAll(plugin));
        this.addSubCommand(new CommandQuestsDelete(plugin));
        this.addSubCommand(new CommandQuestsComplete(plugin));
        this.addSubCommand(new CommandQuestsCompleteAll(plugin));
        this.addSubCommand(new CommandQuestsAddProgress(plugin));
        this.addSubCommand(new CommandQuestsSetProgress(plugin));
        this.addSubCommand(new CommandQuestsHelp(plugin));

        plugin.getConfig().getMapList("main-command-page").forEach(map -> {
            TypedMapAccessor typedMapAccessor = new TypedMapAccessor((Map<String, Object>) map);
            this.questInventoryPages.add(new QuestInventoryPage(typedMapAccessor.getString("permission"), typedMapAccessor.getString("inventory", "quests"), typedMapAccessor.getInt("page", 1), typedMapAccessor.getInt("priority", 0)));
        });
    }

    @Override
    protected CommandType perform(QuestsPlugin plugin) {

        if (sender instanceof Player player) {
            var result = this.questInventoryPages.stream()
                    .filter(questInventoryPage -> player.hasPermission(questInventoryPage.permission()))
                    .max(Comparator.comparingInt(QuestInventoryPage::priority))
                    .orElse(new QuestInventoryPage("", "quests", 1, 0));
            plugin.getQuestManager().openQuestInventory(player, result);
        } else syntaxMessage();

        return CommandType.SUCCESS;
    }

}
