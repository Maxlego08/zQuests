package fr.maxlego08.quests.storage.migrations;

import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.sarah.database.Migration;

public class PlayerFavoriteAmountMigration extends Migration {
    @Override
    public void up() {
        this.create("%prefix%" + Tables.PLAYER_FAVORITE_AMOUNT, table -> {
            table.uuid("unique_id").primary();
            table.integer("amount");
            table.timestamps();
        });
    }
}
