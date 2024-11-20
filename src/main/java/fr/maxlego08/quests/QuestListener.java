package fr.maxlego08.quests;

import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.Parameter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuestListener implements Listener {

    private final QuestsPlugin plugin;
    private final QuestManager manager;

    public QuestListener(QuestsPlugin plugin, QuestManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.manager.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.manager.handleQuit(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        this.manager.handleQuests(event.getPlayer().getUniqueId(), QuestType.BLOCK_BREAK, Parameter.of("blocks", event.getBlock().getType()));
    }

}
