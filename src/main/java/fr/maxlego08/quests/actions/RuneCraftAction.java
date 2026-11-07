package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import java.util.Locale;

public class RuneCraftAction implements QuestAction {
    private final String runeId;

    public RuneCraftAction(String runeId) {
        this.runeId = runeId.trim().toLowerCase(Locale.ROOT);
        if (this.runeId.isEmpty()) throw new IllegalArgumentException("Missing rune id; use '*' for any rune");
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof String id && (runeId.equals("*") || runeId.equals(id));
    }

    @Override
    public QuestType getQuestType() { return QuestType.RUNE_CRAFT; }
}
