package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.event.CancelledQuestEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public class QuestCompleteEvent extends CancelledQuestEvent {

    private final UUID playerUniqueId;
    private final ActiveQuest activeQuest;

    public QuestCompleteEvent(UUID playerUniqueId, ActiveQuest activeQuest) {
        this.playerUniqueId = playerUniqueId;
        this.activeQuest = activeQuest;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public ActiveQuest getActiveQuest() {
        return activeQuest;
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(this.playerUniqueId);
    }
}
