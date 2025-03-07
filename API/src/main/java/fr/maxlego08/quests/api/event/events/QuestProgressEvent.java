package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.event.CancelledQuestEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public class QuestProgressEvent extends CancelledQuestEvent {

    private final UUID playerUniqueId;
    private final ActiveQuest activeQuest;
    private int amount;

    public QuestProgressEvent(UUID playerUniqueId, ActiveQuest activeQuest, int amount) {
        this.playerUniqueId = playerUniqueId;
        this.activeQuest = activeQuest;
        this.amount = amount;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public ActiveQuest getActiveQuest() {
        return activeQuest;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(this.playerUniqueId);
    }
}
