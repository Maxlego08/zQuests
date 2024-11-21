package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.actions.StartQuestAction;

import java.io.File;

public class StartQuestLoader implements ActionLoader {

    private final QuestsPlugin plugin;

    public StartQuestLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getKey() {
        return "start_quest";
    }

    @Override
    public Action load(String path, TypedMapAccessor typedMapAccessor, File file) {
        String questName = typedMapAccessor.getString("quest");
        return new StartQuestAction(plugin, questName);
    }
}
