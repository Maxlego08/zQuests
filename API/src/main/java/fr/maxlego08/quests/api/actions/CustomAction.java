package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class CustomAction extends ActionInfo<String> {

    public CustomAction(String data) {
        super(QuestType.CUSTOM, data);
    }
}
