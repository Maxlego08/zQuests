package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.actions.BrewAction;
import fr.maxlego08.quests.actions.JobAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.List;

public class JobQuestLoader implements QuestActionLoader {

    private final QuestsPlugin plugin;

    public JobQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        String potionName = accessor.getString("job", null);
        return new JobAction(questType, potionName);
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.JOB_LEVEL, QuestType.JOB_PRESTIGE, QuestType.JOB_PRESTIGE_AND_LEVEL);
    }
}
