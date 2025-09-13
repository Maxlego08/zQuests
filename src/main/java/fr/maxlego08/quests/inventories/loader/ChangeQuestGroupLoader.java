package fr.maxlego08.quests.inventories.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.inventories.buttons.ChangeQuestGroupButton;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ChangeQuestGroupLoader extends ButtonLoader {

    private final QuestsPlugin plugin;

    public ChangeQuestGroupLoader(QuestsPlugin plugin) {
        super(plugin, "ZQUESTS_CHANGE_QUEST_GROUP");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        String enableText = configuration.getString(path + "enable-text", "&a%display-name%");
        String disableText = configuration.getString(path + "disable-text", "&c%display-name%");
        String extendEnableText = configuration.getString(path + "extend-enable-text", "  &a%display-name%");
        String extendDisableText = configuration.getString(path + "extend-disable-text", "  &c%display-name%");
        List<String> groups = configuration.getStringList(path + "groups");

        return new ChangeQuestGroupButton(this.plugin, enableText, disableText, extendEnableText, extendDisableText, groups);
    }
}
