package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.actions.ActionInfo;
import fr.maxlego08.quests.api.actions.BrewAction;
import fr.maxlego08.quests.api.actions.CommandAction;
import fr.maxlego08.quests.api.actions.EnchantAction;
import fr.maxlego08.quests.api.actions.EntityAction;
import fr.maxlego08.quests.api.actions.MaterialAction;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.BrewEvent;

public enum QuestType {


    BLOCK_BREAK,
    BLOCK_PLACE,
    ENTITY_KILL,
    FARMING,
    FISHING,
    COMMAND,
    TAME,
    ENCHANT,
    BREW,
    SMELT,
    CRAFT;

    public ActionInfo<?> toAction(Object target) {
        return switch (this) {
            case BLOCK_BREAK, BLOCK_PLACE, FARMING, FISHING, SMELT, CRAFT-> new MaterialAction(this, (Material) target);
            case ENTITY_KILL, TAME -> new EntityAction(this, (EntityType) target);
            case COMMAND -> new CommandAction(target == null ? "" : (String) target);
            case ENCHANT -> new EnchantAction(this, (EnchantItemEvent) target);
            case BREW -> new BrewAction(this, (BrewEvent) target);
        };
    }

    public boolean isMaterial() {
        return switch (this) {
            case BLOCK_BREAK, BLOCK_PLACE, FARMING, FISHING, SMELT, CRAFT -> true;
            case COMMAND, ENTITY_KILL, TAME, ENCHANT, BREW -> false;
        };
    }

    public boolean isEntityType() {
        return this == ENTITY_KILL || this == TAME;
    }

}
