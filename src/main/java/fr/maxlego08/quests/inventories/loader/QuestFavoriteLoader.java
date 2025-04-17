package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.QuestFavoriteButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class QuestFavoriteLoader implements ButtonLoader {

    private final QuestsPlugin plugin;

    public QuestFavoriteLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return QuestFavoriteButton.class;
    }

    @Override
    public String getName() {
        return "ZQUESTS_FAVORITE_TOGGLE";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        List<String> quests = configuration.getStringList(path + "quests");
        String actionEnable = configuration.getString(path + "enable", "Clic to enable");
        String actionDisable = configuration.getString(path + "disable", "Clic to disable");
        return new QuestFavoriteButton(this.plugin, quests, actionEnable, actionDisable);
    }
}
