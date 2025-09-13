package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageAction implements QuestAction {

    @Override
    public QuestType getQuestType() {
        return QuestType.ENTITY_DAMAGE;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof EntityDamageByEntityEvent event && event.getDamage() > 0;
    }
}
