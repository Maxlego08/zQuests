package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

;

public class CommandAction extends ActionInfo<String> {
    public CommandAction(String value) {
        super(QuestType.COMMAND, value);
    }
}
