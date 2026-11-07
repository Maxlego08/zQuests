package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.NumberAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import java.io.File;
import java.util.List;
import java.util.Locale;

public class NumberQuestLoader implements QuestActionLoader {
    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType type, File file) {
        if (type == QuestType.GUILD_NUMBER) {
            String metric = accessor.getString("metric", "members").trim().toLowerCase(Locale.ROOT);
            if (!List.of("members", "level").contains(metric)) throw new IllegalArgumentException("Invalid guild metric in " + file + ": " + metric);
            return new NumberAction(type, metric);
        }
        String mode = accessor.getString("mode", "balance").trim().toLowerCase(Locale.ROOT);
        String currency = accessor.getString("currency", "or").trim().toLowerCase(Locale.ROOT);
        if (!List.of("balance", "earned").contains(mode) || currency.isBlank())
            throw new IllegalArgumentException("Invalid economy mode or currency in " + file);
        return new NumberAction(type, mode + ":" + currency);
    }

    @Override
    public List<QuestType> getSupportedTypes() { return List.of(QuestType.GUILD_NUMBER, QuestType.ECO_NUMBER); }
}
