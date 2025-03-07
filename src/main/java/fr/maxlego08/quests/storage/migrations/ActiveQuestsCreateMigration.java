package fr.maxlego08.quests.storage.migrations;

import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.sarah.database.Migration;

public class ActiveQuestsCreateMigration extends Migration {

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
