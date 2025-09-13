package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class ExperienceGainAction extends ActionInfo<Integer> {
    public ExperienceGainAction(QuestType questType, Integer value) {
        super(questType, value);
    }
}
