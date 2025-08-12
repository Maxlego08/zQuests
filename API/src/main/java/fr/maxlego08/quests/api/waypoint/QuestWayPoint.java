package fr.maxlego08.quests.api.waypoint;

import fr.maxlego08.quests.api.Quest;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface QuestWayPoint {

    UUID getUniqueId();

    void create(Player player);

    void delete(Player player);

    boolean match(Quest quest);

}
