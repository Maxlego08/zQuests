package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class VoteAction extends ActionInfo<String> {

    public VoteAction(QuestType questType) {
        super(questType, "");
    }
}
