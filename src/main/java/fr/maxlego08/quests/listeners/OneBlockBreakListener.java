package fr.maxlego08.quests.listeners;

import fr.kazotaruu.oneBlock.api.OneBlockAPI;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/** ONEBLOCK_BLOCK_BREAK : casse du bloc magique d'une ile OneBlock (le materiau est lu avant sa regeneration). */
public class OneBlockBreakListener implements Listener {
    private final QuestManager manager;

    public OneBlockBreakListener(QuestManager manager) { this.manager = manager; }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        var api = OneBlockAPI.get();
        if (api == null || !api.isMagicBlock(event.getBlock().getLocation())) return;
        manager.handleQuests(event.getPlayer().getUniqueId(), QuestType.ONEBLOCK_BLOCK_BREAK, 1, event.getBlock().getType());
    }
}
