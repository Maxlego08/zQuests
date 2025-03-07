package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.event.CancelledQuestEvent;
import org.bukkit.entity.Player;

public class QuestFavoriteChangeEvent extends CancelledQuestEvent {

    private final Player player;
    private final ActiveQuest activeQuest;
    private boolean isFavorite;

    public QuestFavoriteChangeEvent(Player player, ActiveQuest activeQuest, boolean isFavorite) {
        this.player = player;
        this.activeQuest = activeQuest;
        this.isFavorite = isFavorite;
    }

    public Player getPlayer() {
        return player;
    }

    public ActiveQuest getActiveQuest() {
        return activeQuest;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
