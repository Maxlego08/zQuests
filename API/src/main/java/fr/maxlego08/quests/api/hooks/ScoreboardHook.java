package fr.maxlego08.quests.api.hooks;

import java.util.UUID;

public interface ScoreboardHook {

    void updateScoreboard(UUID playerUniqueId);
}
