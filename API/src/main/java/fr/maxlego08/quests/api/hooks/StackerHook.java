package fr.maxlego08.quests.api.hooks;

import org.bukkit.entity.LivingEntity;

public interface StackerHook {

    /**
     * Retrieves the count of entities stacked with the given living entity.
     *
     * @param livingEntity the living entity to check
     * @return the count of entities stacked with the specified living entity
     */
    int getEntityCount(LivingEntity livingEntity);

}
