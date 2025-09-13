package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.QuestFavoriteButton;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class QuestFavoriteLoader extends ButtonLoader {

    private final QuestsPlugin plugin;

    public QuestFavoriteLoader(QuestsPlugin plugin) {
        super(plugin, "ZQUESTS_FAVORITE_TOGGLE");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        List<String> quests = configuration.getStringList(path + "quests");
        String actionEnable = configuration.getString(path + "enable", "Clic to enable");
        String actionDisable = configuration.getString(path + "disable", "Clic to disable");
        return new QuestFavoriteButton(this.plugin, quests, actionEnable, actionDisable);
    }
}
