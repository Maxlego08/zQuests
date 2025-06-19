package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.actions.StartQuestAction;

import java.io.File;
import java.util.List;

public class StartQuestLoader extends ActionLoader {

    private final QuestsPlugin plugin;

    public StartQuestLoader(QuestsPlugin plugin) {
        super("start_quest", "start quest", "start quests", "start_quests");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor typedMapAccessor, File file) {
        List<String> quests = typedMapAccessor.getStringList("quests");
        return new StartQuestAction(plugin, quests);
    }
}
