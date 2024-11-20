package fr.maxlego08.quests.api.storage;

import fr.maxlego08.quests.api.ActiveQuest;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface StorageManager {

    void loadDatabase();

    void upsert(ActiveQuest activeQuest);

    void delete(ActiveQuest activeQuest);

    void load(UUID uuid, Consumer<List<ActiveQuest>> consumer);
}
