package fr.maxlego08.quests.storage.migrations;

import fr.maxlego08.quests.api.storage.Tables;
import fr.maxlego08.quests.api.storage.dto.ActiveQuestDTO;
import fr.maxlego08.sarah.database.Migration;

public class ActiveQuestsCreateMigration extends Migration {
    
    @Override
    public void up() {
        this.createOrAlter("%prefix%" + Tables.ACTIVE_QUESTS, ActiveQuestDTO.class);
    }
}
