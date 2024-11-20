package fr.maxlego08.quests.api;

import java.util.List;

public interface UserQuest {

    List<ActiveQuest> getActiveQuests();

    List<CompletedQuest> getCompletedQuests();

}
