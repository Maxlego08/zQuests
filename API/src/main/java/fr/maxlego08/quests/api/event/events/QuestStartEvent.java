package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.event.CancelledQuestEvent;

import java.util.UUID;

/**
 * Called when a player starts a quest.
 *
 * @author Maxlego08
 */
public class QuestStartEvent extends CancelledQuestEvent {

    private final UUID playerUUID;
    private ActiveQuest activeQuest;

    public QuestStartEvent(UUID playerUUID, ActiveQuest activeQuest) {
        this.playerUUID = playerUUID;
        this.activeQuest = activeQuest;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public ActiveQuest getActiveQuest() {
        return activeQuest;
    }

    public void setActiveQuest(ActiveQuest activeQuest) {
        this.activeQuest = activeQuest;
    }
}