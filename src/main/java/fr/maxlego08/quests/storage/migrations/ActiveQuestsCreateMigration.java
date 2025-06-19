package fr.maxlego08.quests.storage.migrations;

import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.sarah.database.Migration;

public class ActiveQuestsCreateMigration extends Migration {

    /**
     * Creates the ACTIVE_QUESTS table with specified columns and constraints.
     * The table includes a unique UUID as the primary key, a name as a primary
     * key, an amount as a big integer, an is_favorite boolean with a default
     * value of false, and timestamps for record tracking.
     */
    @Override
    public void up() {
        this.create("%prefix%" + Tables.ACTIVE_QUESTS, table -> {
            table.uuid("unique_id").primary();
            table.string("name", 255).primary();
            table.bigInt("amount");
            table.bool("is_favorite").defaultValue(false);
            table.timestamps();
        });
    }
}
