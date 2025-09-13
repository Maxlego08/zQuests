package fr.maxlego08.quests.api.hooks;

import java.util.UUID;

public interface ScoreboardHook {

    /**
     * Updates the scoreboard for the given player.
     *
     * @param playerUniqueId the unique identifier of the player
     */
    void updateScoreboard(UUID playerUniqueId);
}
