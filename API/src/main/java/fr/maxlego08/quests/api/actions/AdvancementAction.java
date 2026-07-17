package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class AdvancementAction extends ActionInfo<String> {

    public AdvancementAction(String advancement) {
        super(QuestType.ADVANCEMENT, advancement);
    }
}
