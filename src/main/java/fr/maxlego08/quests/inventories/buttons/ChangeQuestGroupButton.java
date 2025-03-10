package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.save.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChangeQuestGroupButton extends ZButton {

    private final QuestsPlugin plugin;
    private final String enableText;
    private final String disableText;
    private final List<String> groups;

    public ChangeQuestGroupButton(QuestsPlugin plugin, String enableText, String disableText, List<String> groups) {
        this.plugin = plugin;
        this.enableText = enableText;
        this.disableText = disableText;
        this.groups = groups;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        Placeholders placeholders = new Placeholders();

        UserQuest userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        String currentGroup = userQuest.getCurrentGroup();

        for (String groupName : groups) {
            var optional = this.plugin.getQuestManager().getGroups(groupName);
            String displayName = groupName.equalsIgnoreCase("all") ? (currentGroup == null ? this.enableText : this.disableText).replace("%display-name%", Config.globalGroupName) : optional.map(group -> (groupName.equals(currentGroup) ? this.enableText : this.disableText).replace("%display-name%", group.getDisplayName())).orElse(groupName);

            placeholders.register(groupName, displayName);
        }

        return getItemStack().build(player, false, placeholders);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        UserQuest userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        String currentGroup = userQuest.getCurrentGroup();

        String currentGroupName = currentGroup == null ? "all" : currentGroup;

        int index = this.groups.indexOf(currentGroupName);
        currentGroup = event.isRightClick() ? this.groups.get(index - 1 < 0 ? this.groups.size() - 1 : index - 1) : this.groups.get(index + 1 >= this.groups.size() ? 0 : index + 1);

        userQuest.setCurrentGroup(currentGroup.equalsIgnoreCase("all") ? null : currentGroup);
        this.plugin.getInventoryManager().updateInventory(player);
    }
}
