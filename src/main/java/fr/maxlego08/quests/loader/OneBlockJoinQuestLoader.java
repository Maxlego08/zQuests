package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.OneBlockJoinAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import java.io.File;
import java.util.List;
import java.util.Locale;

public class OneBlockJoinQuestLoader implements QuestActionLoader {
    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType type, File file) {
        String role = accessor.getString("role", "*").trim().toLowerCase(Locale.ROOT);
        if (!List.of("chef", "membre", "*").contains(role)) {
            throw new IllegalArgumentException("Unknown OB_JOIN role '" + role + "' in " + file + ", use chef, membre or *");
        }
        return new OneBlockJoinAction(role);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.OB_JOIN);
    }
}
