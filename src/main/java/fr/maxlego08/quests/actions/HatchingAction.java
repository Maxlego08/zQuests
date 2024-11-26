package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.Egg;

public class HatchingAction implements QuestAction {

    @Override
    public boolean isAction(Object target) {
        return target instanceof Egg;
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.HATCHING;
    }
}
