package fr.maxlego08.quests.api.hooks;

import org.bukkit.block.Block;

public interface BlockHook {

    boolean isTracked(Block block);

}