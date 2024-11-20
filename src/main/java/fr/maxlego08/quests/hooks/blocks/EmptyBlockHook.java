package fr.maxlego08.quests.hooks.blocks;

import fr.maxlego08.quests.api.hooks.BlockHook;
import org.bukkit.block.Block;

public class EmptyBlockHook implements BlockHook {

    @Override
    public boolean isTracked(Block block) {
        return false;
    }
}