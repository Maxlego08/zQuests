package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.CancelledQuestEvent;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;

public class QuestFavoriteChangePlaceholderTypeEvent extends CancelledQuestEvent {

    private final UserQuest userQuest;
    private FavoritePlaceholderType newFavoritePlaceholderType;

    public QuestFavoriteChangePlaceholderTypeEvent(UserQuest userQuest, FavoritePlaceholderType newFavoritePlaceholderType) {
        this.userQuest = userQuest;
        this.newFavoritePlaceholderType = newFavoritePlaceholderType;
    }

    public UserQuest getUserQuest() {
        return userQuest;
    }

    public FavoritePlaceholderType getNewFavoritePlaceholderType() {
        return newFavoritePlaceholderType;
    }

    public void setNewFavoritePlaceholderType(FavoritePlaceholderType newFavoritePlaceholderType) {
        this.newFavoritePlaceholderType = newFavoritePlaceholderType;
    }
}
