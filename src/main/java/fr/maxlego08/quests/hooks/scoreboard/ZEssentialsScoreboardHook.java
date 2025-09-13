package fr.maxlego08.quests.hooks.scoreboard;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.quests.api.hooks.ScoreboardHook;
import fr.maxlego08.quests.zcore.utils.plugins.Plugins;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ZEssentialsScoreboardHook implements ScoreboardHook {

    @Override
    public void updateScoreboard(UUID playerUniqueId) {

        var player = Bukkit.getPlayer(playerUniqueId);
        if (player == null) return;

        EssentialsPlugin essentialsPlugin = (EssentialsPlugin) Bukkit.getPluginManager().getPlugin(Plugins.ZESSENTIALS.getName());
        if (essentialsPlugin == null) return;

        var manager = essentialsPlugin.getScoreboardManager();
        manager.update(player);
    }
}
