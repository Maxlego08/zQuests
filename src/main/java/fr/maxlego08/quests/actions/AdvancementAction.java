package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

import java.util.List;

public class AdvancementAction implements QuestAction {

    private final List<String> advancements;

    public AdvancementAction(List<String> advancements) {
        this.advancements = advancements;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof String key && this.advancements.stream().anyMatch(key::equalsIgnoreCase);
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.ADVANCEMENT;
    }
}
