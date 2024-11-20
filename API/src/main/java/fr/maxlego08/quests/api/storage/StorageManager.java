package fr.maxlego08.quests.api.storage;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.UserQuest;

import java.util.UUID;
import java.util.function.Consumer;

public interface StorageManager {

    void loadDatabase();

    void upsert(ActiveQuest activeQuest);

    void delete(ActiveQuest activeQuest);

    void load(UUID uuid, Consumer<UserQuest> consumer);

    void softUpsert(ActiveQuest activeQuest);

    void upsert(UUID uuid, CompletedQuest completedQuest);
}
