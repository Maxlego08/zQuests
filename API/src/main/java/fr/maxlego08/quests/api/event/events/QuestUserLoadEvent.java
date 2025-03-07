package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.QuestEvent;

public class QuestUserLoadEvent extends QuestEvent {

    private final UserQuest userQuest;

    public QuestUserLoadEvent(UserQuest userQuest) {
        this.userQuest = userQuest;
    }

    public UserQuest getUserQuest() {
        return userQuest;
    }
}
