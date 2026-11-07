package fr.maxlego08.quests.listeners;

import fr.kazotaruu.oneBlock.api.event.OneBlockProgressEvent;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OneBlockProgressListener implements Listener {
    private final QuestManager manager;

    public OneBlockProgressListener(QuestManager manager) { this.manager = manager; }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onProgress(OneBlockProgressEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        QuestType type = switch (event.getKind()) {
            case LEVEL -> QuestType.ONEBLOCK_LEVEL;
            case PRESTIGE -> QuestType.ONEBLOCK_PRESTIGE;
            case PHASE -> QuestType.ONEBLOCK_PHASE;
        };
        manager.handleQuests(event.getPlayer().getUniqueId(), type,
                (int) Math.min(Integer.MAX_VALUE, event.getAmount()),
                event.getKind() == OneBlockProgressEvent.Kind.PHASE ? event.getPhaseId() : "*");
    }
}
