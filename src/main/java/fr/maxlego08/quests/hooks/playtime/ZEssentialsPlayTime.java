package fr.maxlego08.quests.hooks.playtime;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.utils.PlayTimeHelper;
import fr.maxlego08.quests.zcore.utils.SpigotPlayTime;

import java.util.UUID;

public class ZEssentialsPlayTime implements PlayTimeHelper {

    private final QuestsPlugin plugin;
    private final PlayTimeHelper fallbackPlayTime = new SpigotPlayTime();
    private EssentialsPlugin essentialsPlugin;

    public ZEssentialsPlayTime(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public long getPlayTime(UUID uuid) {
        var user = getEssentialsPlugin().getUser(uuid);
        return user == null ? this.fallbackPlayTime.getPlayTime(uuid) : user.getPlayTime();
    }

    private EssentialsPlugin getEssentialsPlugin() {
        return this.essentialsPlugin == null ? this.essentialsPlugin = this.plugin.getProvider(EssentialsPlugin.class) : this.essentialsPlugin;
    }
}
