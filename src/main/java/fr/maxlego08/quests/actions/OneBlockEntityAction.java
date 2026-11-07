package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

/** Matches the persistent mob identifier written by OneBlock's MobService. */
public class OneBlockEntityAction implements QuestAction {

    private static final NamespacedKey MOB_KEY = new NamespacedKey("oneblock", "mob");

    private final String mobId;
    private final QuestType questType;

    public OneBlockEntityAction(String mobId, QuestType questType) {
        this.mobId = mobId;
        this.questType = questType;
    }

    @Override
    public QuestType getQuestType() {
        return questType;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof Entity entity
                && this.mobId.equals(entity.getPersistentDataContainer().get(MOB_KEY, PersistentDataType.STRING));
    }
}
