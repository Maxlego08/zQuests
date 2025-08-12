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

    void loadGlobalConfiguration();

    Optional<List<HologramConfiguration>> getConfiguration(String name);
}
