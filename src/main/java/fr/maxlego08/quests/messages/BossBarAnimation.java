package fr.maxlego08.quests.messages;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.zcore.ZPlugin;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Set;

public class BossBarAnimation implements Runnable {

    private final Player player;
    private final BossBar bossBar;
    private final WrappedTask wrappedTask;
    private final long duration;
    private long remainingTicks;

    public BossBarAnimation(ZPlugin plugin, Player player, BossBar bossBar, long duration) {
        this.player = player;
        this.bossBar = bossBar;
        this.remainingTicks = duration;
        this.duration = duration;
        this.wrappedTask = plugin.getScheduler().runTimer(this, 0L, 1L);
    }

    public static void from(QuestsPlugin plugin, Player player, String message, float v, BossBar.Color color, BossBar.Overlay overlay, Set<BossBar.Flag> flags, long duration) {

        var component = LegacyComponentSerializer.builder().build().deserialize(message);
        BossBar bossBar = BossBar.bossBar(component, v, color, overlay, flags);
        player.showBossBar(bossBar);

        new BossBarAnimation(plugin, player, bossBar, duration);
    }

    @Override
    public void run() {
        double progress = (double) this.remainingTicks / this.duration;
        this.bossBar.progress((float) progress);

        if (remainingTicks <= 0) {
            this.player.hideBossBar(this.bossBar);
            this.wrappedTask.cancel();
        }

        this.remainingTicks -= 1;
    }
}