package fr.maxlego08.quests.listeners;

import fr.kazotaruu.oneBlock.api.OneBlockAPI;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Bukkit;

/** Cache-only check: completes OB_JOIN quests as soon as the player has an island, also when he had one before the quest started. */
public class OneBlockJoinTracker implements Runnable {
    private final QuestManager manager;
    public OneBlockJoinTracker(QuestManager manager) { this.manager = manager; }

    @Override
    public void run() {
        var api = OneBlockAPI.get();
        if (api == null) return;
        for (var player : Bukkit.getOnlinePlayers()) {
            if (player.hasMetadata("NPC")) continue;
            var uuid = player.getUniqueId();
            api.islandOf(uuid).ifPresent(island ->
                NumberQuestProgress.update(manager, uuid, QuestType.OB_JOIN, 1, uuid.equals(island.owner()) ? "chef" : "membre"));
        }
    }
}
