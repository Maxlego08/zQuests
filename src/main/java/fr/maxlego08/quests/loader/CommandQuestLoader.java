package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.CommandAction;
import fr.maxlego08.quests.actions.JobAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class CommandQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public CommandQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        List<String> commands = accessor.getStringList("commands", Collections.emptyList());
        return new CommandAction(commands);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.COMMAND);
    }
}
