package fr.maxlego08.quests.api.waypoint;

import fr.maxlego08.quests.api.event.QuestEvent;
import org.bukkit.event.Listener;

public interface WayPointManager extends Listener {

    void onQuestEvent(QuestEvent event);

}
