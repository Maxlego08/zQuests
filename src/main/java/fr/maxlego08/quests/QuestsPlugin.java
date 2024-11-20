package fr.maxlego08.quests;


import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.storage.StorageManager;
import fr.maxlego08.quests.commands.commands.CommandQuests;
import fr.maxlego08.quests.placeholder.LocalPlaceholder;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.storage.ZStorageManager;
import fr.maxlego08.quests.zcore.ZPlugin;

/**
 * System to create your plugins very simple Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class QuestsPlugin extends ZPlugin {

    private final StorageManager storageManager = new ZStorageManager(this);
    private final QuestManager questManager = new ZQuestManager(this);
    private InventoryManager inventoryManager;
    private ButtonManager buttonManager;

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zquests");

        this.preEnable();

        this.saveDefaultConfig();
        Config.getInstance().load(this.getConfig());

        this.inventoryManager = getProvider(InventoryManager.class);
        this.buttonManager = getProvider(ButtonManager.class);

        this.registerCommand("zquests", new CommandQuests(this), "quests", "q");

        this.loadFiles();
        this.questManager.loadQuests();
        this.storageManager.loadDatabase();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ButtonManager getButtonManager() {
        return buttonManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }
}
