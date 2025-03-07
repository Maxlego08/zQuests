package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.QuestHistoryButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class QuestHistoryLoader implements ButtonLoader {

    private final QuestsPlugin plugin;

    public QuestHistoryLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return QuestHistoryButton.class;
    }

    @Override
    public String getName() {
        return "ZQUESTS_HISTORY";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        List<Integer> offsetSlots = configuration.getIntegerList(path + "offset-slots");
        boolean enabledCompleted = configuration.getBoolean(path + "enable-completed", true);
        int offsetCustomModelId = configuration.getInt(path + "offset-custom-model-id", 0);

        File file = new File(plugin.getDataFolder(), "inventories/quest-history.yml");
        MenuItemStack completedQuest = load(configuration, path + "completed-item.", file);
        QuestHistoryButton.FavConfiguration favConfiguration = new QuestHistoryButton.FavConfiguration(
                configuration.getInt(path + "favorite.offset", 0),
                load(configuration, path + "favorite.enable.", file),
                load(configuration, path + "favorite.disable.", file),
                load(configuration, path + "favorite.completed.", file)
        );

        return new QuestHistoryButton(this.plugin, offsetSlots, completedQuest, enabledCompleted, offsetCustomModelId, favConfiguration);
    }

    private MenuItemStack load(YamlConfiguration configuration, String path, File file) {
        try {
            Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());
            return loader.load(configuration, path, file);
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
