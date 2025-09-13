package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.entity.Egg;

public class HatchingAction extends ActionInfo<Egg>
{

    public HatchingAction(QuestType questType, Egg value) {
        super(questType, value);
    }
}
