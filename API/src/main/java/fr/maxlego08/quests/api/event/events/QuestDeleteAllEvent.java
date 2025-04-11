package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.QuestEvent;

public class QuestDeleteAllEvent extends QuestEvent {

    private final UserQuest userQuest;

    public QuestDeleteAllEvent(UserQuest userQuest) {
        this.userQuest = userQuest;
    }

    public UserQuest getUserQuest() {
        return userQuest;
    }
}
