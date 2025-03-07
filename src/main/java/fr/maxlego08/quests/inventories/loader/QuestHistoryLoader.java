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
        Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());
        MenuItemStack completedQuest = null;
        try {
            completedQuest = loader.load(configuration, path + "completed-item.", new File(plugin.getDataFolder(), "inventories/quest-history.yml"));
        } catch (InventoryException e) {
            throw new RuntimeException(e);
        }

        return new QuestHistoryButton(this.plugin, offsetSlots, completedQuest, enabledCompleted);
    }
}
