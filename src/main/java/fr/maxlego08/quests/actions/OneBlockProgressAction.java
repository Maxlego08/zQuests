package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

public class OneBlockProgressAction implements QuestAction {
    private final QuestType type;
    private final String phase;

    public OneBlockProgressAction(QuestType type, String phase) {
        this.type = type;
        this.phase = phase.trim();
        if (this.phase.isEmpty()) throw new IllegalArgumentException("Missing phase id; use '*' for any phase");
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof String value && (phase.equals("*") || phase.equals(value));
    }

    @Override
    public QuestType getQuestType() { return type; }
}
