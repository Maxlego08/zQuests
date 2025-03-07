package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.event.QuestEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public class QuestPostProgressEvent extends QuestEvent {

    private final UUID playerUniqueId;
    private final int count;

    public QuestPostProgressEvent(UUID playerUniqueId, int count) {
        this.playerUniqueId = playerUniqueId;
        this.count = count;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public int getCount() {
        return count;
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(this.playerUniqueId);
    }
}
