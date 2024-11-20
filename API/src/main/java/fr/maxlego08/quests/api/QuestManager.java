package fr.maxlego08.quests.api;

import java.io.File;
import java.util.List;

public interface QuestManager {

    void loadQuests();

    List<Quest> loadQuests(File file);

    List<Quest> getQuests();

}
