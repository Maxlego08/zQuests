package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.ShopsAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import java.io.File;
import java.util.List;

public class ShopsQuestLoader implements QuestActionLoader {
    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType type, File file) {
        return new ShopsAction(type, accessor.getString("item", "*"));
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.BUY_SHOP, QuestType.SELL_SHOP, QuestType.AH_BUY, QuestType.ORDER_COMPLETE);
    }
}
