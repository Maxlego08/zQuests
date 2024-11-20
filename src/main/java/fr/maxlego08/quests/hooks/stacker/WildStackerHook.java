package fr.maxlego08.quests.hooks.stacker;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import fr.maxlego08.quests.api.hooks.StackerHook;
import org.bukkit.entity.LivingEntity;

public class WildStackerHook implements StackerHook {
    @Override
    public int getEntityCount(LivingEntity livingEntity) {
        return WildStackerAPI.getEntityAmount(livingEntity);
    }
}
