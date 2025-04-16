package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.hologram.HologramConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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
    private final List<Action> startActions;
    private final List<QuestAction> actions;
    private final boolean useGlobalRewards;
    private final boolean canChangeFavorite;
    private final boolean isFavorite;
    private final int customModelId;
    private final boolean isUnique;
    private final boolean isHidden;
    private final List<HologramConfiguration> hologramConfigurations;

    public ZQuest(QuestsPlugin plugin, String name, QuestType type, String displayName, String description, Material thumbnail, long goal, boolean autoAccept, List<Action> rewards, List<Action> startActions, List<QuestAction> actions, boolean useGlobalRewards, boolean canChangeFavorite, boolean isFavorite, int customModelId, boolean isUnique, boolean isHidden, List<HologramConfiguration> hologramConfiguration) {
        this.plugin = plugin;
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.thumbnail = thumbnail;
        this.goal = goal;
        this.autoAccept = autoAccept;
        this.rewards = rewards;
        this.startActions = startActions;
        this.actions = actions;
        this.useGlobalRewards = useGlobalRewards;
        this.canChangeFavorite = canChangeFavorite;
        this.isFavorite = isFavorite;
        this.customModelId = customModelId;
        this.isUnique = isUnique;
        this.isHidden = isHidden;
        this.hologramConfigurations = hologramConfiguration;
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
        if (player == null) {
            this.plugin.getLogger().severe("[COMPLETE] Player not found: " + activeQuest.getUniqueId() + ", unable to handle rewards for quest " + activeQuest.getQuest().getName());
            return;
        }

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
    public List<Action> getStartActions() {
        return this.startActions;
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

    @Override
    public int getCustomModelId() {
        return this.customModelId;
    }

    @Override
    public boolean isUnique() {
        return this.isUnique;
    }

    @Override
    public boolean isHidden() {
        return this.isHidden;
    }

    @Override
    public List<HologramConfiguration> getHologramConfigurations() {
        return this.hologramConfigurations;
    }

    @Override
    public boolean hasHologram() {
        return !this.hologramConfigurations.isEmpty();
    }

    @Override
    public String getHologramName(UUID uuid) {
        return String.format("zquests-%s-%s", this.name, uuid);
    }
}
