package fr.maxlego08.quests.api.waypoint;

import fr.maxlego08.quests.api.event.QuestEvent;
import org.bukkit.event.Listener;

import java.util.Optional;

public interface WayPointManager extends Listener {

    void onQuestEvent(QuestEvent event);

    void loadGlobalConfiguration();

    Optional<WayPointConfiguration> getConfiguration(String name);

}
