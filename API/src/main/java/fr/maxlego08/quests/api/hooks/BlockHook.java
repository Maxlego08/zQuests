package fr.maxlego08.quests.api.hooks;

import org.bukkit.block.Block;

public interface BlockHook {

    /**
     * Checks if the given block is tracked by a quest.
     * @param block the block to check
     * @return true if the block is tracked, false otherwise
     */
    boolean isTracked(Block block);

}