package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

public class ShopsAction implements QuestAction {
    private final QuestType type;
    private final String item;

    public ShopsAction(QuestType type, String item) {
        this.type = type;
        this.item = item.trim();
        if (this.item.isEmpty()) throw new IllegalArgumentException("Missing item; use '*' for all items");
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof String key && (item.equals("*") || item.equalsIgnoreCase(key));
    }

    @Override
    public QuestType getQuestType() { return type; }
}
