package fr.maxlego08.quests.api.storage;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.UserQuest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Interface for managing storage operations related to quests.
 */
public interface StorageManager {

    /**
     * Load the database.
     */
    void loadDatabase();

    /**
     * Upsert an active quest in the storage.
     *
     * @param activeQuest the active quest to upsert
     */
    void upsert(ActiveQuest activeQuest);

    /**
     * Delete an active quest from the storage.
     *
     * @param activeQuest the active quest to delete
     */
    void delete(ActiveQuest activeQuest);

    /**
     * Delete all quests associated with a user.
     *
     * @param uuid the unique identifier of the user
     */
    void deleteAll(UUID uuid, Runnable runnable);

    /**
     * Load user quest data.
     *
     * @param uuid     the unique identifier of the user
     * @param consumer the consumer to handle the loaded user quest data
     */
    void load(UUID uuid, Consumer<UserQuest> consumer);

    /**
     * Soft upsert an active quest in the storage.
     *
     * @param activeQuest the active quest to soft upsert
     */
    void softUpsert(ActiveQuest activeQuest);

    /**
     * Upsert a completed quest for a user.
     *
     * @param uuid           the unique identifier of the user
     * @param completedQuest the completed quest to upsert
     */
    void upsert(UUID uuid, CompletedQuest completedQuest);

    /**
     * Delete a quest by unique identifier and name.
     *
     * @param uniqueId the unique identifier of the quest
     * @param name     the name of the quest
     */
    void deleteQuest(@NotNull UUID uniqueId, String name);

    /**
     * Upsert multiple active quests in the storage.
     *
     * @param activeQuests the active quests to upsert
     */
    void upsert(List<ActiveQuest> activeQuests);

    /**
     * Delete a completed quest from the storage.
     *
     * @param uniqueId       the unique identifier of the user
     * @param completedQuest the completed quest to delete
     */
    void delete(UUID uniqueId, CompletedQuest completedQuest);
}