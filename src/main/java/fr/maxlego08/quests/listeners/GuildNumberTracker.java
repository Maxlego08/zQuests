package fr.maxlego08.quests.listeners;

import fr.mjarena.guilde.api.GuildeAPI;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Bukkit;

/** Cache-only checks also handle reconnects and newly started quests. */
public class GuildNumberTracker implements Runnable {
    private final QuestManager manager;
    public GuildNumberTracker(QuestManager manager) { this.manager = manager; }

    @Override
    public void run() {
        var api = Bukkit.getServicesManager().load(GuildeAPI.class);
        if (api == null) return;
        for (var player : Bukkit.getOnlinePlayers()) {
            if (player.hasMetadata("NPC")) continue;
            var guild = api.guildOf(player.getUniqueId());
            NumberQuestProgress.update(manager, player.getUniqueId(), QuestType.GUILD_NUMBER, guild == null ? 0 : guild.size(), "members");
            NumberQuestProgress.update(manager, player.getUniqueId(), QuestType.GUILD_NUMBER, guild == null ? 0 : guild.level(), "level");
        }
    }
}
