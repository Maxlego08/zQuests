package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;

public class RuneForgeAction extends ActionInfo<String> {
    public RuneForgeAction(String runeId) { super(QuestType.RUNE_FORGE, runeId); }
}
