package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.AdvancementAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class AdvancementQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public AdvancementQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        List<String> advancements = accessor.getStringList("advancements", Collections.emptyList());
        return new AdvancementAction(advancements);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.ADVANCEMENT);
    }
}
