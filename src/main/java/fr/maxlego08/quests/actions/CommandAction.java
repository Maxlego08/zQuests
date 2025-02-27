package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

import java.util.List;

public class CommandAction implements QuestAction {

    private final List<String> commands;

    public CommandAction(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof String string && this.commands.stream().anyMatch(string::startsWith);
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.COMMAND;
    }
}
