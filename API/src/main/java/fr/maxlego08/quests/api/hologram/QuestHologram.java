package fr.maxlego08.quests.api.hologram;

import org.bukkit.entity.Player;

public interface QuestHologram {

    /**
     * Creates the hologram.
     * This method initializes and displays the hologram for all relevant players.
     */
    void create();

    /**
     * Creates the hologram for the given player.
     *
     * @param player The player to create the hologram for.
     */
    void create(Player player);

    /**
     * Deletes the hologram from the player's view.
     *
     * @param player The player to delete the hologram from.
     */
    void delete(Player player);

    /**
     * Updates the hologram's appearance for the given player.
     *
     * @param player The player to update the hologram's appearance for.
     */
    void update(Player player);

    /**
     * Checks if the hologram matches the given name.
     *
     * @param name The name to match against the hologram.
     * @return true if the hologram matches the name, false otherwise.
     */
    boolean match(String name);
}
