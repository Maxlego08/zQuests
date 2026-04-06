package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class PlayerKillAction extends ActionInfo<Integer> {

    public PlayerKillAction(QuestType questType) {
        super(questType, 0);
    }
}
