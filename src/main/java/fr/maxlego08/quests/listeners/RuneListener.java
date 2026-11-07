package fr.maxlego08.quests.listeners;

import fr.kazotaruu.runes.api.event.RuneCraftEvent;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RuneListener implements Listener {
    private final QuestManager manager;

    public RuneListener(QuestManager manager) { this.manager = manager; }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraft(RuneCraftEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        manager.handleQuests(event.getPlayer().getUniqueId(), QuestType.RUNE_CRAFT, 1, event.getRuneId());
    }
}
