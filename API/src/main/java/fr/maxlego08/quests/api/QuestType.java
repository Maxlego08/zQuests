package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.actions.ActionInfo;
import fr.maxlego08.quests.api.actions.BrewAction;
import fr.maxlego08.quests.api.actions.CommandAction;
import fr.maxlego08.quests.api.actions.CustomAction;
import fr.maxlego08.quests.api.actions.EnchantAction;
import fr.maxlego08.quests.api.actions.EntityAction;
import fr.maxlego08.quests.api.actions.EntityDamageAction;
import fr.maxlego08.quests.api.actions.ExperienceGainAction;
import fr.maxlego08.quests.api.actions.HatchingAction;
import fr.maxlego08.quests.api.actions.InventoryContentAction;
import fr.maxlego08.quests.api.actions.InventoryOpenAction;
import fr.maxlego08.quests.api.actions.IslandAction;
import fr.maxlego08.quests.api.actions.ItemStackAction;
import fr.maxlego08.quests.api.actions.JobAction;
import fr.maxlego08.quests.api.actions.LocationAction;
import fr.maxlego08.quests.api.actions.MaterialAction;
import fr.maxlego08.quests.api.actions.ResurrectAction;
import fr.maxlego08.quests.api.actions.VoteAction;
import fr.maxlego08.quests.api.utils.InventoryContent;
import org.bukkit.Location;
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
    PURCHASE,
    ENTITY_DAMAGE,
    EXPERIENCE_GAIN,
    HATCHING,
    ITEM_BREAK,
    ITEM_MENDING,
    RESURRECT,
    JOB_LEVEL,
    JOB_PRESTIGE,
    SMITHING,
    ISLAND,
    COMMAND,
    CUBOID,
    CUSTOM,
    INVENTORY_OPEN,
    INVENTORY_CONTENT,
    ;

    public ActionInfo<?> toAction(Object target) {
        return switch (this) {
            case BLOCK_BREAK, BLOCK_PLACE, FARMING, FISHING, SMELT, SELL, PURCHASE, ITEM_BREAK, ITEM_MENDING, SMITHING -> new MaterialAction(this, (Material) target);
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
            case ISLAND -> new IslandAction(target);
            case COMMAND -> new CommandAction((String) target);
            case CUBOID -> new LocationAction(this, (Location) target);
            case CUSTOM -> new CustomAction((String) target);
            case INVENTORY_OPEN -> new InventoryOpenAction((String) target);
            case INVENTORY_CONTENT -> new InventoryContentAction((InventoryContent) target);
        };
    }

    public boolean isMaterial() {
        return switch (this) {
            case BLOCK_BREAK, BLOCK_PLACE, FARMING, FISHING, SMELT, SELL, ITEM_BREAK, ITEM_MENDING, SMITHING, PURCHASE -> true;
            default -> false;
        };
    }

    public boolean isEntityType() {
        return this == ENTITY_KILL || this == TAME;
    }

}
