package fr.maxlego08.quests;

import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.Parameter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        Player player = event.getPlayer();
        Material material = block.getType();

        this.manager.handleQuests(player.getUniqueId(), QuestType.BLOCK_BREAK, Parameter.of("blocks", event.getBlock().getType()));

        if (!(block.getBlockData() instanceof Ageable)) {

            if (this.plugin.getBlockHook().isTracked(block)) return;

            this.manager.handleQuests(player.getUniqueId(), QuestType.BLOCK_BREAK, Parameter.of("blocks", event.getBlock().getType()));

        } else if (block.getBlockData() instanceof Ageable ageable && ((material == Material.SUGAR_CANE || material == Material.KELP || material == Material.BAMBOO) || ageable.getAge() == ageable.getMaximumAge())) {

            this.manager.handleQuests(player.getUniqueId(), QuestType.FARMING, Parameter.of("blocks", event.getBlock().getType()));
        }
    }

}
