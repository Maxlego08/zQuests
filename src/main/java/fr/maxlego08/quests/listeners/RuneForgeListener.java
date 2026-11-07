package fr.maxlego08.quests.listeners;

import fr.kazotaruu.runes.api.event.RuneForgeEvent;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RuneForgeListener implements Listener {
    private final QuestManager manager;
    public RuneForgeListener(QuestManager manager) { this.manager = manager; }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onForge(RuneForgeEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        manager.handleQuests(event.getPlayer().getUniqueId(), QuestType.RUNE_FORGE, 1, event.getRuneId());
    }
}
