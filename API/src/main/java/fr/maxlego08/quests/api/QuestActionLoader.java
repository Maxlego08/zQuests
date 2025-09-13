package fr.maxlego08.quests.api;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;

import java.io.File;
import java.util.List;

public interface QuestActionLoader {

    QuestAction load(TypedMapAccessor accessor, QuestType questType, File file);

    List<QuestType> getSupportedTypes();

}
