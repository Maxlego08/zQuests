package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.EntityType;

public class EntityAction extends ActionInfo<EntityType> {
    public EntityAction(QuestType questType, EntityType value) {
        super(questType, value);
    }
}
