package fr.maxlego08.quests.waypoint;

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
import fr.maxlego08.quests.api.waypoint.WayPointManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ZWayPointManager implements WayPointManager {

    private final QuestsPlugin plugin;

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

        var questWayPoint = new EssentialsWayPoint(this.plugin, quest);
        questWayPoint.create(player);

        userQuest.addWayPoint(questWayPoint);
    }
}
