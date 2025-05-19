package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.inventories.buttons.QuestCompleteButton;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Optional;

public class QuestCompleteLoader extends ButtonLoader {

    private final QuestsPlugin plugin;

    public QuestCompleteLoader(QuestsPlugin plugin) {
        super(plugin, "ZQUESTS_COMPLETE");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        List<Quest> quests = configuration.getStringList(path + "quests").stream().map(questName -> this.plugin.getQuestManager().getQuest(questName)).filter(Optional::isPresent).map(Optional::get).toList();

        return new QuestCompleteButton(this.plugin, quests);
    }
}
