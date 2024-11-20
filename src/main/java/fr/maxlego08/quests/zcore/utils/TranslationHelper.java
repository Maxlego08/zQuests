package fr.maxlego08.quests.zcore.utils;

import org.bukkit.inventory.ItemStack;

/**
 * Abstract helper class for translating item names.
 * Provides methods for getting the translated name of an item stack, optionally using the zTranslator plugin.
 */
public abstract class TranslationHelper {

    /**
     * Translates the name of an item stack.
     * If the item stack has a display name, it returns the display name.
     * If the zTranslator plugin is active, it retrieves the translated name from the plugin.
     * Otherwise, it returns a formatted version of the item type name.
     *
     * @param itemStack the item stack whose name is to be translated.
     * @return the translated item name, or an empty string if the item stack is null.
     */
    protected String getItemName(ItemStack itemStack) {
        if (itemStack == null) {
            return "";
        }
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }

        String name = itemStack.serialize().get("type").toString().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
