package fr.maxlego08.quests.actions;

import fr.maxlego08.jobs.api.Job;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

public class JobAction implements QuestAction {

    private final QuestType questType;
    private final String jobName;

    public JobAction(QuestType questType, String jobName) {
        this.questType = questType;
        this.jobName = jobName;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof Job job && job.getFileName().equals(jobName);
    }

    @Override
    public QuestType getQuestType() {
        return this.questType;
    }
}
