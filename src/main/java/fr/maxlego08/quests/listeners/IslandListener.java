package fr.maxlego08.quests.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.event.events.QuestStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class IslandListener implements Listener {

    private final QuestManager manager;

    public IslandListener(QuestManager manager) {
        this.manager = manager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIslandCreate(IslandCreateEvent event) {
        var player = event.getPlayer();
        this.manager.handleStaticQuests(player.getUniqueId(), QuestType.ISLAND, 1, event.getIsland());
    }

    @EventHandler(ignoreCancelled = true)
    public void onIslandJoin(IslandJoinEvent event) {
        var player = event.getPlayer();
        this.manager.handleStaticQuests(player.getUniqueId(), QuestType.ISLAND, 1, event.getIsland());
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuestStart(QuestStartEvent event) {

        var activeQuest = event.getActiveQuest();
        var quest = activeQuest.getQuest();

        if (quest.getType() == QuestType.ISLAND) {

            var superiorPlayer = SuperiorSkyblockAPI.getPlayer(activeQuest.getUniqueId());
            if (superiorPlayer == null) return;

            var island = superiorPlayer.getIsland();
            if (island == null) return;

            if (activeQuest.incrementStatic(1)) {
                event.setCancelled(true);
                manager.completeQuest(activeQuest);
            }
        }
    }
}
