package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.ChangeQuestGroupButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ChangeQuestGroupLoader implements ButtonLoader {

    private final QuestsPlugin plugin;

    public ChangeQuestGroupLoader(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return ChangeQuestGroupButton.class;
    }

    @Override
    public String getName() {
        return "ZQUESTS_CHANGE_QUEST_GROUP";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        String enableText = configuration.getString(path + "enable-text", "&a%display-name%");
        String disableText = configuration.getString(path + "disable-text", "&c%display-name%");
        List<String> groups = configuration.getStringList(path + "groups");

        return new ChangeQuestGroupButton(this.plugin, enableText, disableText, groups);
    }
}
