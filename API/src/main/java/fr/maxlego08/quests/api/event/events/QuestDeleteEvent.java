package fr.maxlego08.quests.api.event.events;

import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.QuestEvent;

public class QuestDeleteEvent extends QuestEvent {

    private final UserQuest userQuest;
    private final Quest quest;

    public QuestDeleteEvent(UserQuest userQuest, Quest quest) {
        this.userQuest = userQuest;
        this.quest = quest;
    }

    public UserQuest getUserQuest() {
        return userQuest;
    }

    public Quest getQuest() {
        return quest;
    }
}
