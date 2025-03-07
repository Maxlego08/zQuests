package fr.maxlego08.quests.storage.migrations;

import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.sarah.database.Migration;

public class CompletedQuestsCreateMigration extends Migration {

    @Override
    public void up() {
        this.create("%prefix%" + Tables.COMPLETED_QUESTS, table -> {
            table.uuid("unique_id").primary();
            table.string("name", 255).primary();
            table.timestamp("started_at");
            table.timestamp("completed_at");
        });
    }
}
