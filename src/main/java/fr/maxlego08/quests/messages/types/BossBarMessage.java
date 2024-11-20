package fr.maxlego08.quests.messages.types;


import fr.maxlego08.quests.messages.MessageType;
import fr.maxlego08.quests.zcore.ZPlugin;
import net.kyori.adventure.bossbar.BossBar;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record BossBarMessage(String text, String color, String overlay, List<String> flags, long duration,
                             boolean isStatic) implements ZMessage {

    @Override
    public MessageType messageType() {
        return MessageType.BOSSBAR;
    }

    public boolean isValid(ZPlugin plugin) {

        try {
            BossBar.Color.valueOf(color);
        } catch (Exception ignored) {
            plugin.getLogger().severe("BossBar Color " + color + " doesn't exit !");
            return false;
        }

        try {
            BossBar.Overlay.valueOf(overlay);
        } catch (Exception ignored) {
            plugin.getLogger().severe("BossBar Overlay " + overlay + " doesn't exit !");
            return false;
        }

        for (String flag : flags) {
            try {
                BossBar.Flag.valueOf(flag);
            } catch (Exception ignored) {
                plugin.getLogger().severe("BossBar Flag " + flag + " doesn't exit !");
                return false;
            }
        }

        return true;
    }

    public BossBar.Overlay getOverlay() {
        return BossBar.Overlay.valueOf(this.overlay);
    }

    public BossBar.Color getColor() {
        return BossBar.Color.valueOf(this.color);
    }

    public Set<BossBar.Flag> getFlags() {
        return this.flags.stream().map(BossBar.Flag::valueOf).collect(Collectors.toSet());
    }
}