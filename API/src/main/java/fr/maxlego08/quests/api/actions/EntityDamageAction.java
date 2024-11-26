package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageAction extends ActionInfo<EntityDamageByEntityEvent> {
    public EntityDamageAction(QuestType questType, EntityDamageByEntityEvent value) {
        super(questType, value);
    }
}
