package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.SetFavoriteLimitButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SetFavoriteLimitLoader extends ButtonLoader {

    private final QuestsPlugin plugin;

    public SetFavoriteLimitLoader(QuestsPlugin plugin) {
        super(plugin, "ZQUESTS_SET_FAVORITE_LIMIT");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        int amount = Integer.parseInt(configuration.getString(path + "limit", "1"));
        return new SetFavoriteLimitButton(this.plugin, amount);
    }
}
