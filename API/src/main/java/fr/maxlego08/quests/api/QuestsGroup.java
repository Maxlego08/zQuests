package fr.maxlego08.quests.api;

import java.util.List;

public interface QuestsGroup {

    String getName();

    String getDisplayName();

    List<Quest> getQuests();

}
