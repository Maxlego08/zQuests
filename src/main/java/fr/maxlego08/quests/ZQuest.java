package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class ZQuest implements Quest {

    private final QuestsPlugin plugin;
    private final String name;
    private final QuestType type;
    private final String displayName;
    private final String description;
    private final Material thumbnail;
    private final long goal;
    private final boolean autoAccept;
    private final List<Action> rewards;
    private final List<QuestAction> actions;
    private final boolean useGlobalRewards;
    private final boolean canChangeFavorite;
    private final boolean isFavorite;

    public ZQuest(QuestsPlugin plugin, String name, QuestType type, String displayName, String description, Material thumbnail, long goal, boolean autoAccept, List<Action> rewards, List<QuestAction> actions, boolean useGlobalRewards, boolean canChangeFavorite, boolean isFavorite) {
        this.plugin = plugin;
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.thumbnail = thumbnail;
        this.goal = goal;
        this.autoAccept = autoAccept;
        this.rewards = rewards;
        this.actions = actions;
        this.useGlobalRewards = useGlobalRewards;
        this.canChangeFavorite = canChangeFavorite;
        this.isFavorite = isFavorite;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public QuestType getType() {
        return type;
    }

    @Override
    public void onComplete(ActiveQuest activeQuest) {

        Player player = Bukkit.getPlayer(activeQuest.getUniqueId());
        if (player == null) return;

        this.rewards.forEach(reward -> reward.preExecute(player, null, this.plugin.getInventoryManager().getFakeInventory(), new Placeholders()));
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Material getThumbnail() {
        return thumbnail == null ? Material.PAPER : thumbnail;
    }

    @Override
    public long getGoal() {
        return goal;
    }

    @Override
    public boolean isAutoAccept() {
        return autoAccept;
    }

    @Override
    public List<Action> getRewards() {
        return rewards;
    }

    @Override
    public List<QuestAction> getActions() {
        return actions;
    }

    @Override
    public boolean useGlobalRewards() {
        return this.useGlobalRewards;
    }

    @Override
    public boolean canChangeFavorite() {
        return this.canChangeFavorite;
    }

    @Override
    public boolean isFavorite() {
        return this.isFavorite;
    }
}
