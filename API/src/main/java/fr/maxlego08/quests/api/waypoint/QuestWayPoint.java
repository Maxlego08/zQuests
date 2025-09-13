package fr.maxlego08.quests.api.waypoint;

import fr.maxlego08.quests.api.Quest;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface QuestWayPoint {

    /**
     * Gets the unique identifier for this waypoint.
     *
     * @return The waypoint's UUID.
     */
    UUID getUniqueId();

    /**
     * Creates the waypoint for the given player.
     *
     * @param player The player to create the waypoint for.
     */
    void create(Player player);

    /**
     * Deletes the waypoint for the given player.
     *
     * @param player The player to delete the waypoint from.
     */
    void delete(Player player);

    /**
     * Checks if the given quest matches the quest that this waypoint is
     * associated with.
     *
     * @param quest The quest to check.
     * @return True if the quests match, false otherwise.
     */
    boolean match(Quest quest);

    /**
     * Gets the quest that this waypoint is associated with.
     *
     * @return The quest associated with this waypoint.
     */
    Quest getQuest();

}
