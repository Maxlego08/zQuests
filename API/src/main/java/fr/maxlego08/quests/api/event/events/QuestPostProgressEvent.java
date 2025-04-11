package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.event.QuestEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class QuestPostProgressEvent extends QuestEvent {

    private final UUID playerUniqueId;
    private final Set<ActiveQuest> activeQuests;

    public QuestPostProgressEvent(UUID playerUniqueId, Set<ActiveQuest> activeQuests) {
        this.playerUniqueId = playerUniqueId;
        this.activeQuests = activeQuests;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public int getCount() {
        return this.activeQuests.size();
    }

    public Set<ActiveQuest> getActiveQuests() {
        return activeQuests;
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(this.playerUniqueId);
    }
}
