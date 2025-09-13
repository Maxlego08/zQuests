package fr.maxlego08.quests.api.waypoint;

import org.bukkit.Location;

import java.awt.*;

public record WayPointConfiguration(Location location, String texture, Color color) {
}
