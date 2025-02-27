package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.zcore.utils.Cuboid;
import org.bukkit.Location;

public class CuboidAction implements QuestAction {

    private final Cuboid cuboid;

    public CuboidAction(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof Location location && this.cuboid.contains(location);
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.CUBOID;
    }
}
