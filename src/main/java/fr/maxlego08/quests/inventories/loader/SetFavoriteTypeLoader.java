package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.quests.inventories.buttons.SetFavoriteLimitButton;
import fr.maxlego08.quests.inventories.buttons.SetFavoriteTypeButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SetFavoriteTypeLoader implements ButtonLoader {

    private final QuestsPlugin plugin;

    public SetFavoriteTypeLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return SetFavoriteLimitButton.class;
    }

    @Override
    public String getName() {
        return "ZQUESTS_SET_FAVORITE_TYPE";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        FavoritePlaceholderType favoritePlaceholderType = FavoritePlaceholderType.valueOf(configuration.getString(path + "type", FavoritePlaceholderType.LARGE.name()).toUpperCase());
        return new SetFavoriteTypeButton(this.plugin, favoritePlaceholderType);
    }
}
