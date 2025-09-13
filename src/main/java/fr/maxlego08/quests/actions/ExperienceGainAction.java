package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

public class ExperienceGainAction implements QuestAction {

    @Override
    public boolean isAction(Object target) {
        return target instanceof Integer integer && integer > 0;
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.EXPERIENCE_GAIN;
    }
}
