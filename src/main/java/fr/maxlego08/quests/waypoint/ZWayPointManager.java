package fr.maxlego08.quests.waypoint;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.QuestEvent;
import fr.maxlego08.quests.api.event.events.QuestCompleteEvent;
import fr.maxlego08.quests.api.event.events.QuestDeleteAllEvent;
import fr.maxlego08.quests.api.event.events.QuestDeleteEvent;
import fr.maxlego08.quests.api.event.events.QuestStartEvent;
import fr.maxlego08.quests.api.event.events.QuestUserLoadEvent;
import fr.maxlego08.quests.api.waypoint.WayPointConfiguration;
import fr.maxlego08.quests.api.waypoint.WayPointManager;
import fr.maxlego08.quests.zcore.utils.Colors;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ZWayPointManager extends ZUtils implements WayPointManager {

    private final QuestsPlugin plugin;
    private final Map<String, WayPointConfiguration> wayPoints = new HashMap<>();

    public ZWayPointManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onQuestEvent(QuestEvent event) {
        if (event instanceof QuestStartEvent startEvent) {
            this.onQuestStart(startEvent.getActiveQuest(), startEvent.getPlayerUUID());
        } else if (event instanceof QuestUserLoadEvent userLoadEvent) {
            this.onQuestLoad(userLoadEvent.getUserQuest());
        } else if (event instanceof QuestCompleteEvent completeEvent) {
            this.onQuestComplete(completeEvent.getPlayer(), completeEvent.getActiveQuest());
        } else if (event instanceof QuestDeleteEvent questDeleteEvent) {
            this.onQuestDelete(questDeleteEvent.getUserQuest(), questDeleteEvent.getQuest());
        } else if (event instanceof QuestDeleteAllEvent questDeleteAllEvent) {
            this.onQuestDeleteAll(questDeleteAllEvent.getUserQuest());
        }
    }

    @Override
    public void loadGlobalConfiguration() {

        File file = new File(this.plugin.getDataFolder(), "waypoints.yml");
        if (!file.exists()) {
            this.plugin.saveResource("waypoints.yml", false);
        }

        this.wayPoints.clear();

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.getMapList("waypoints").forEach(map -> {

            var accessor = new TypedMapAccessor((Map<String, Object>) map);

            var location = changeStringLocationToLocation(accessor.getString("location"));
            String texture = accessor.contains("texture") ? accessor.getString("texture") : null;
            Color color = Colors.parseColor(accessor.getString("color", "white"));
            var name = accessor.getString("name");
            var waypointConfiguration = new WayPointConfiguration(location, texture, color);

            this.wayPoints.put(name, waypointConfiguration);
        });
    }

    @Override
    public Optional<WayPointConfiguration> getConfiguration(String name) {
        return Optional.ofNullable(this.wayPoints.get(name));
    }

    private void onQuestDeleteAll(UserQuest userQuest) {
        var player = Bukkit.getPlayer(userQuest.getUniqueId());
        if (player == null) return;

        for (ActiveQuest activeQuest : userQuest.getActiveQuests()) {
            this.deleteWayPoint(player, activeQuest.getQuest());
        }
    }

    private void onQuestDelete(UserQuest userQuest, Quest quest) {
        var player = Bukkit.getPlayer(userQuest.getUniqueId());
        if (player == null) return;

        this.deleteWayPoint(player, quest);
    }

    private void onQuestComplete(Player player, ActiveQuest activeQuest) {
        deleteWayPoint(player, activeQuest.getQuest());
    }

    private void onQuestLoad(UserQuest userQuest) {

        var player = Bukkit.getPlayer(userQuest.getUniqueId());
        if (player == null) return;

        for (ActiveQuest activeQuest : userQuest.getActiveQuests()) {
            this.createWayPoint(player, activeQuest.getQuest());
        }
    }

    private void onQuestStart(ActiveQuest activeQuest, UUID playerUUID) {

        var player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        this.createWayPoint(player, activeQuest.getQuest());
    }

    private void deleteWayPoint(Player player, Quest quest) {

        if (!quest.hasWayPoint()) return;

        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        var optional = userQuest.getWayPoint(quest);
        if (optional.isEmpty()) return;

        var questWayPoint = optional.get();
        questWayPoint.delete(player);

        userQuest.removeWayPoint(questWayPoint);

    }

    private void createWayPoint(Player player, Quest quest) {

        if (!quest.hasWayPoint()) return;

        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        if (userQuest.getWayPoint(quest).isPresent()) return;

        if (quest.getWayPointConfiguration().location().getWorld() != player.getWorld()) return;

        var questWayPoint = new EssentialsWayPoint(this.plugin, quest);
        questWayPoint.create(player);

        userQuest.addWayPoint(questWayPoint);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        var player = event.getPlayer();
        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());

        var fromWorld = event.getFrom();
        var toWorld = event.getPlayer().getWorld();

        userQuest.getQuestWayPoints().forEach(wayPoint -> {
            if (wayPoint.getQuest().getWayPointConfiguration().location().getWorld().equals(fromWorld)) {
                wayPoint.delete(player);
            } else {
                wayPoint.create(player);
            }
        });
    }
}
