package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.CancelledQuestEvent;

public class QuestFavoriteChangeAmountEvent extends CancelledQuestEvent {

    private final UserQuest userQuest;
    private int newAmount;

    public QuestFavoriteChangeAmountEvent(UserQuest userQuest, int newAmount) {
        this.userQuest = userQuest;
        this.newAmount = newAmount;
    }

    public UserQuest getUserQuest() {
        return userQuest;
    }

    public int getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }
}
