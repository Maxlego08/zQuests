package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.CancelledQuestEvent;

public class QuestFavoriteChangeLimitEvent extends CancelledQuestEvent {

    private final UserQuest userQuest;
    private int newLimit;

    public QuestFavoriteChangeLimitEvent(UserQuest userQuest, int newLimit) {
        this.userQuest = userQuest;
        this.newLimit = newLimit;
    }

    public UserQuest getUserQuest() {
        return userQuest;
    }

    public int getNewLimit() {
        return newLimit;
    }

    public void setNewLimit(int newLimit) {
        this.newLimit = newLimit;
    }
}
