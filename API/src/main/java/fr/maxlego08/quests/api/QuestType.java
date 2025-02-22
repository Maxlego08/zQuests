package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.actions.*;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;

public enum QuestType {

    BLOCK_BREAK,
    BLOCK_PLACE,
    ENTITY_KILL,
    FARMING,
    FISHING,
    TAME,
    ENCHANT,
    BREW,
    SMELT,
    CRAFT,
    VOTE,
    SELL,
    ENTITY_DAMAGE,
    EXPERIENCE_GAIN,
    HATCHING,
    ITEM_BREAK,
    ITEM_MENDING,
    RESURRECT,
    JOB_LEVEL,
    JOB_PRESTIGE,
    SMITHING,
    SELLING,
    ;

    public ActionInfo<?> toAction(Object target) {
        return switch (this) {
            case BLOCK_BREAK, BLOCK_PLACE, FARMING, FISHING, SMELT, SELL, ITEM_BREAK, ITEM_MENDING, SMITHING, SELLING -> new MaterialAction(this, (Material) target);
            case CRAFT -> new ItemStackAction(this, (ItemStack) target);
            case ENTITY_KILL, TAME -> new EntityAction(this, (EntityType) target);
            case ENCHANT -> new EnchantAction(this, (EnchantItemEvent) target);
            case BREW -> new BrewAction(this, (BrewEvent) target);
            case VOTE -> new VoteAction(this);
            case HATCHING -> new HatchingAction(this, (Egg) target);
            case ENTITY_DAMAGE -> new EntityDamageAction(this, (EntityDamageByEntityEvent) target);
            case EXPERIENCE_GAIN -> new ExperienceGainAction(this, (Integer) target);
            case RESURRECT -> new ResurrectAction(this);
            case JOB_LEVEL, JOB_PRESTIGE -> new JobAction(this, (String) target);
        };
    }

    public boolean isMaterial() {
        return switch (this) {
            case BLOCK_BREAK, BLOCK_PLACE, FARMING, FISHING, SMELT, SELL, ITEM_BREAK, ITEM_MENDING, SMITHING, SELLING -> true;
            default -> false;
        };
    }

    public boolean isEntityType() {
        return this == ENTITY_KILL || this == TAME;
    }

}
