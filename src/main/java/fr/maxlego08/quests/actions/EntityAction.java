package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityAction implements QuestAction {

    private final EntityType target;
    private final QuestType questType;

    public EntityAction(EntityType target, QuestType questType) {
        this.target = target;
        this.questType = questType;
    }

    @Override
    public QuestType getQuestType() {
        return questType;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof Entity entity && this.target == entity.getType();
    }
}
