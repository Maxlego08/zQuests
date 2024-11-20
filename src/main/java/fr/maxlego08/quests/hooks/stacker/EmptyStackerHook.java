package fr.maxlego08.quests.hooks.stacker;

import fr.maxlego08.quests.api.hooks.StackerHook;
import org.bukkit.entity.LivingEntity;

public class EmptyStackerHook implements StackerHook {

    @Override
    public int getEntityCount(LivingEntity livingEntity) {
        return 1;
    }
}