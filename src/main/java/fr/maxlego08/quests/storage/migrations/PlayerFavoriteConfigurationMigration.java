package fr.maxlego08.quests.storage.migrations;

import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.sarah.database.Migration;

public class PlayerFavoriteConfigurationMigration extends Migration {
    @Override
    public void up() {
        this.create("%prefix%" + Tables.PLAYER_FAVORITE_CONFIGURATION, table -> {
            table.uuid("unique_id").primary();
            table.integer("limit");
            table.string("placeholder_type", 255);
            table.timestamps();
        });
    }
}
