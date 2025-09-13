package fr.maxlego08.quests.inventories.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.save.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChangeQuestGroupButton extends Button {

    private final QuestsPlugin plugin;
    private final String enableText;
    private final String disableText;
    private final String extendEnableText;
    private final String extendDisableText;
    private final List<String> groups;

    public ChangeQuestGroupButton(QuestsPlugin plugin, String enableText, String disableText, String extendEnableText, String extendDisableText, List<String> groups) {
        this.plugin = plugin;
        this.enableText = enableText;
        this.disableText = disableText;
        this.extendEnableText = extendEnableText;
        this.extendDisableText = extendDisableText;
        this.groups = groups;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        Placeholders placeholders = new Placeholders();

        UserQuest userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        String currentGroup = userQuest.getCurrentGroup();

        for (String groupName : groups) {
            var optional = this.plugin.getQuestManager().getGroup(groupName);
            String displayName;
            if (groupName.equalsIgnoreCase("all")) {

                displayName = (currentGroup == null ? this.enableText : this.disableText).replace("%display-name%", Config.globalGroupName);

            } else {

                StringBuilder builder = new StringBuilder();

                if (optional.isEmpty()) {

                    displayName = this.disableText.replace("%display-name%", groupName);

                } else {

                    var group = optional.get();
                    if (group.contains(currentGroup)) {

                        if (userQuest.isExtend()) {

                            builder.append(this.disableText.replace("%display-name%", group.getDisplayName()));
                            builder.append("\n");

                            for (int i = 0; i != group.getSubGroups().size(); i++) {

                                var sub = group.getSubGroups().get(i);
                                builder.append((currentGroup != null && currentGroup.equalsIgnoreCase(sub.getName()) ? this.extendEnableText : this.extendDisableText).replace("%display-name%", sub.getDisplayName()));

                                if (i < group.getSubGroups().size() - 1) {
                                    builder.append("\n");
                                }
                            }

                        } else {

                            builder.append(this.enableText.replace("%display-name%", group.getDisplayName()));

                        }

                    } else {

                        builder.append(this.disableText.replace("%display-name%", group.getDisplayName()));
                    }

                    displayName = builder.toString();
                }
            }

            placeholders.register(groupName, displayName);
        }

        return getItemStack().build(player, false, placeholders);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {

        var click = event.getClick();

        super.onClick(player, event, inventory, slot, placeholders);

        var manager = this.plugin.getQuestManager();
        UserQuest userQuest = manager.getUserQuest(player.getUniqueId());
        String currentGroup = userQuest.getCurrentGroup();

        String currentGroupName = currentGroup == null ? "all" : currentGroup;

        if (userQuest.isExtend()) {

            var optional = manager.getGroup().values().stream().filter(e -> !e.getSubGroups().isEmpty() && e.contains(currentGroupName)).findFirst();
            if (optional.isEmpty()) return;

            var group = optional.get();

            if (event.isRightClick()) {

                userQuest.setExtend(false);
                userQuest.setCurrentGroup(group.getName());
                this.plugin.getInventoryManager().updateInventory(player);

            } else {

                var subs = group.getSubGroups();
                var optionalSub = subs.stream().filter(e -> e.getName().equalsIgnoreCase(currentGroupName)).findFirst();
                if (optionalSub.isEmpty()) return;
                var sub = optionalSub.get();

                int index = subs.indexOf(sub);
                var newSubGroup = subs.get(index + 1 >= subs.size() ? 0 : index + 1);

                userQuest.setCurrentGroup(newSubGroup.getName());
                this.plugin.getInventoryManager().updateInventory(player);
            }

        } else {

            if (event.isRightClick()) {

                if (currentGroupName.equalsIgnoreCase("all")) return;

                var optional = this.plugin.getQuestManager().getGroup(currentGroupName);
                if (optional.isEmpty()) return;

                var group = optional.get();
                var subs = group.getSubGroups();
                if (subs.isEmpty()) return;

                userQuest.setExtend(true);
                userQuest.setCurrentGroup(subs.getFirst().getName());
                this.plugin.getInventoryManager().updateInventory(player);

            } else {

                int index = this.groups.indexOf(currentGroupName);
                // currentGroup = event.isRightClick() ? this.groups.get(index - 1 < 0 ? this.groups.size() - 1 : index - 1) : this.groups.get(index + 1 >= this.groups.size() ? 0 : index + 1);
                currentGroup = this.groups.get(index + 1 >= this.groups.size() ? 0 : index + 1);

                userQuest.setCurrentGroup(currentGroup.equalsIgnoreCase("all") ? null : currentGroup);
                this.plugin.getInventoryManager().updateInventory(player);
            }
        }
    }
}
