package fr.maxlego08.quests;


import fr.maxlego08.quests.commands.commands.CommandQuests;
import fr.maxlego08.quests.placeholder.LocalPlaceholder;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.zcore.ZPlugin;

/**
 * System to create your plugins very simple Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class QuestsPlugin extends ZPlugin {

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zquests");

        this.preEnable();

        this.saveDefaultConfig();
        Config.getInstance().load(this.getConfig());

        this.registerCommand("zquests", new CommandQuests(this), "quests", "q");

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

}
