package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestType;

public class RuneForgeAction extends RuneCraftAction {
    public RuneForgeAction(String runeId) { super(runeId); }
    @Override
    public QuestType getQuestType() { return QuestType.RUNE_FORGE; }
}
