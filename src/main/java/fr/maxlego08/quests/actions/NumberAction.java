package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

public class NumberAction implements QuestAction {
    private final QuestType type;
    private final String key;

    public NumberAction(QuestType type, String key) { this.type = type; this.key = key; }
    public String getKey() { return key; }
    @Override
    public boolean isAction(Object target) { return key.equals(target); }
    @Override
    public QuestType getQuestType() { return type; }
}
