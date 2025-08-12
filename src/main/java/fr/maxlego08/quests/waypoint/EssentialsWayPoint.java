package fr.maxlego08.quests.waypoint;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.waypoint.WayPointIcon;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.waypoint.QuestWayPoint;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EssentialsWayPoint implements QuestWayPoint {

    private final QuestsPlugin plugin;
    private final UUID uniqueId;
    private final Quest quest;
    private EssentialsPlugin essentialsPlugin;

    public EssentialsWayPoint(QuestsPlugin plugin, Quest quest) {
        this.plugin = plugin;
        this.uniqueId = UUID.randomUUID();
        this.quest = quest;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public void create(Player player) {

        var helper = this.getEssentialsPlugin().getWayPointHelper();
        var config = this.quest.getWayPointConfiguration();

        helper.addWayPoint(player, this.uniqueId, config.location(), WayPointIcon.of(config.texture(), config.color()));
    }

    @Override
    public void delete(Player player) {
        var helper = this.getEssentialsPlugin().getWayPointHelper();
        helper.removeWayPoint(player, this.uniqueId);
    }

    @Override
    public boolean match(Quest quest) {
        return this.quest.getName().equals(quest.getName());
    }

    private EssentialsPlugin getEssentialsPlugin() {
        return this.essentialsPlugin == null ? this.essentialsPlugin = this.plugin.getProvider(EssentialsPlugin.class) : this.essentialsPlugin;
    }
}
