package fr.maxlego08.quests.api.hologram;

import fr.maxlego08.quests.api.event.QuestEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;

public interface HologramManager extends Listener {

    /**
     * Handles the occurrence of a quest-related event.
     *
     * @param event The quest event to be processed.
     */
    void onQuestEvent(QuestEvent event);

    /**
     * Refreshes the hologram for the specified player, updating the information displayed.
     *
     * @param player The player whose hologram is to be refreshed.
     */
    void refreshHologram(Player player);

    /**
     * Loads the global configuration of the hologram manager, which includes the hologram's
     * visibility, refresh rate, and other settings.
     */
    void loadGlobalConfiguration();

    /**
     * Returns a list of hologram configurations with the given name, or an empty optional if
     * no such configurations exist.
     *
     * @param name The name of the configuration to retrieve.
     * @return An optional containing a list of hologram configurations with the given name.
     */
    Optional<List<HologramConfiguration>> getConfiguration(String name);
}
