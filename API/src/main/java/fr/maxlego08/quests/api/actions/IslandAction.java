package fr.maxlego08.quests.api.actions;

import com.bgsoftware.superiorskyblock.api.island.Island;
import fr.maxlego08.quests.api.QuestType;

public class IslandAction extends ActionInfo<Island> {
    public IslandAction(Object value) {
        super(QuestType.ISLAND, (Island) value);
    }
}
