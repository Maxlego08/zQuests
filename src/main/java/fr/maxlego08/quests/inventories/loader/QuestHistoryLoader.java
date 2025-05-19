package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.QuestHistoryButton;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class QuestHistoryLoader extends ButtonLoader {

    private final QuestsPlugin plugin;

    public QuestHistoryLoader(QuestsPlugin plugin) {
        super(plugin, "ZQUESTS_HISTORY");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        List<Integer> offsetSlots = configuration.getIntegerList(path + "offset-slots");
        int offsetCustomModelId = configuration.getInt(path + "offset-custom-model-id", 0);

        File file = new File(plugin.getDataFolder(), "inventories/quest-history.yml");
        MenuItemStack completedQuest = load(configuration, path + "completed-item.", file);

        MenuItemStack additionalInformationItem = load(configuration, path + "additional-information.item.", file);
        int additionalInformationOffset = configuration.getInt(path + "additional-information.offset", -1);
        boolean enableAdditionalInformation = configuration.getBoolean(path + "additional-information.enable", false);

        QuestHistoryButton.FavConfiguration favConfiguration = new QuestHistoryButton.FavConfiguration(
                configuration.getInt(path + "favorite.offset", 0),
                load(configuration, path + "favorite.enable.", file),
                load(configuration, path + "favorite.disable.", file),
                load(configuration, path + "favorite.completed.", file),
                configuration.getString(path + "favorite.lore-enable", "&cClic to disable"),
                configuration.getString(path + "favorite.lore-disable", "&aClic to enable"),
                configuration.getString(path + "favorite.lore-cancel", "&cYou cant change the favorite status")
        );

        return new QuestHistoryButton(this.plugin, offsetSlots, completedQuest, offsetCustomModelId, favConfiguration, additionalInformationItem, additionalInformationOffset, enableAdditionalInformation);
    }

    private MenuItemStack load(YamlConfiguration configuration, String path, File file) {
        return this.plugin.getInventoryManager().loadItemStack(configuration, path, file);
    }
}
