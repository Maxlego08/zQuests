package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.SetFavoriteAmountButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SetFavoriteAmountLoader implements ButtonLoader {

    private final QuestsPlugin plugin;

    public SetFavoriteAmountLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return SetFavoriteAmountButton.class;
    }

    @Override
    public String getName() {
        return "ZQUESTS_SET_FAVORITE_AMOUNT";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        int amount = configuration.getInt(path + "amount", 1);
        return new SetFavoriteAmountButton(this.plugin, amount);
    }
}
