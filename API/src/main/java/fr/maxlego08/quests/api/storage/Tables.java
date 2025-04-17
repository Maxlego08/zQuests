package fr.maxlego08.quests.api.storage;

/**
 * Represents the different tables in the database.
 */
public interface Tables {

    /**
     * The table name for the active quests.
     */
    String ACTIVE_QUESTS = "active_quests";
    /**
     * The table name for the completed quests.
     */
    String COMPLETED_QUESTS = "completed_quests";

    /**
     * The table name for the player favorite quest amount.
     */
    String PLAYER_FAVORITE_AMOUNT = "player_favorite_amount";

}