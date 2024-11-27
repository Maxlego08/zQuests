package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class JobAction extends ActionInfo<Integer> {
    public JobAction(QuestType questType) {
        super(questType, 0);
    }
}
