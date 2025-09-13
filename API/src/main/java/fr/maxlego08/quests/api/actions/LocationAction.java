package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Location;

public class LocationAction extends ActionInfo<Location> {
    public LocationAction(QuestType questType, Location value) {
        super(questType, value);
    }
}
