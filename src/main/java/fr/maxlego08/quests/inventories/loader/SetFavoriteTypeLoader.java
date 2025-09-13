package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;
import fr.maxlego08.quests.inventories.buttons.SetFavoriteTypeButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class SetFavoriteTypeLoader extends ButtonLoader {

    private final QuestsPlugin plugin;

    public SetFavoriteTypeLoader(QuestsPlugin plugin) {
        super(plugin, "ZQUESTS_SET_FAVORITE_TYPE");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        FavoritePlaceholderType favoritePlaceholderType = FavoritePlaceholderType.valueOf(configuration.getString(path + "type", FavoritePlaceholderType.LARGE.name()).toUpperCase());
        return new SetFavoriteTypeButton(this.plugin, favoritePlaceholderType);
    }
}
