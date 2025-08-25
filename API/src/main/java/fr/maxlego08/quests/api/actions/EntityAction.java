package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityAction extends ActionInfo<Entity> {
    public EntityAction(QuestType questType, Entity value) {
        super(questType, value);
    }
}
