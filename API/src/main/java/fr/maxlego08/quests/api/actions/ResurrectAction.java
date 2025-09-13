package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.Egg;

public class ResurrectAction extends ActionInfo<Integer>
{

    public ResurrectAction(QuestType questType) {
        super(questType, 0);
    }
}
