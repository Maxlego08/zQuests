package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class JobAction extends ActionInfo<String> {

    public JobAction(QuestType questType, String jobName) {
        super(questType, jobName);
    }
}
