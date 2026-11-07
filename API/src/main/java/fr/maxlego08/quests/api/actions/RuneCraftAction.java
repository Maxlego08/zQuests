package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class RuneCraftAction extends ActionInfo<String> {
    public RuneCraftAction(String runeId) {
        super(QuestType.RUNE_CRAFT, runeId);
    }
}
