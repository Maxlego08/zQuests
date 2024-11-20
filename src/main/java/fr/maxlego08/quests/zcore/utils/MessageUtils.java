package fr.maxlego08.quests.zcore.utils;

import fr.maxlego08.quests.messages.BossBarAnimation;
import fr.maxlego08.quests.messages.MessageType;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.messages.types.BossBarMessage;
import fr.maxlego08.quests.messages.types.ClassicMessage;
import fr.maxlego08.quests.messages.types.TitleMessage;
import fr.maxlego08.quests.zcore.ZPlugin;
import fr.maxlego08.quests.zcore.enums.Permission;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageUtils extends LocationUtils {

    public static String getString(String message, Object[] newArgs) {
        if (newArgs.length % 2 != 0) {
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");
        }

        for (int i = 0; i < newArgs.length; i += 2) {
            if (newArgs[i] == null || newArgs[i + 1] == null) {
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            }
            message = message.replace(newArgs[i].toString(), newArgs[i + 1].toString());
        }
        return message;
    }

    protected void message(UUID uniqueId, Message message, Object... args) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) return;
        message(player, message, args);
    }

    protected void broadcast(Permission permission, Message message, Object... args) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(permission.asPermission())) {
                message(player, message, args);
            }
        });
        message(Bukkit.getConsoleSender(), message, args);
    }

    protected void broadcast(Message message, Object... args) {
        Bukkit.getOnlinePlayers().forEach(player -> message(player, message, args));
        message(Bukkit.getConsoleSender(), message, args);
    }

    protected void message(CommandSender sender, Message message, Object... args) {

        if (sender instanceof Player player) {
            message.getMessages().forEach(essentialsMessage -> {

                if (essentialsMessage instanceof ClassicMessage classicMessage) {

                    switch (essentialsMessage.messageType()) {
                        case TCHAT, WITHOUT_PREFIX -> sendTchatMessage(sender, classicMessage, args);
                        case ACTION -> classicMessage.messages().forEach(currentMessage -> {
                            sender.sendActionBar(getComponentMessage(currentMessage, args));
                        });
                        case CENTER -> classicMessage.messages().forEach(currentMessage -> {
                            sender.sendMessage(getCenteredMessage(getMessage(currentMessage, args)));
                        });
                    }

                } else if (essentialsMessage instanceof BossBarMessage bossBarMessage) {
                    BossBar bossBar = BossBar.bossBar(getComponent(papi(bossBarMessage.text(), player)), 1f, bossBarMessage.getColor(), bossBarMessage.getOverlay(), bossBarMessage.getFlags());
                    player.showBossBar(bossBar);

                    new BossBarAnimation(JavaPlugin.getPlugin(ZPlugin.class), player, bossBar, bossBarMessage.duration());
                } else if (essentialsMessage instanceof TitleMessage titleMessage) {
                    Component title = getComponent(papi(getMessage(titleMessage.title(), args), player));
                    Component subtitle = getComponent(papi(getMessage(titleMessage.subtitle(), args), player));

                    player.showTitle(Title.title(title, subtitle, Title.Times.times(Duration.ofMillis(titleMessage.start()), Duration.ofMillis(titleMessage.time()), Duration.ofMillis(titleMessage.end()))));
                }
            });
        } else {
            message.getMessages().forEach(essentialsMessage -> {
                if (essentialsMessage instanceof ClassicMessage classicMessage) {
                    sendTchatMessage(sender, classicMessage, args);
                }
            });
        }
    }

    private void sendTchatMessage(CommandSender sender, ClassicMessage classicMessage, Object... args) {
        boolean isWithoutPrefix = classicMessage.messageType() == MessageType.WITHOUT_PREFIX || classicMessage.messages().size() > 1;
        classicMessage.messages().forEach(message -> sender.sendMessage((isWithoutPrefix ? "" : Message.PREFIX.getMessageAsString()) + getMessage(message, args)));
    }

    public Component getComponentMessage(Message message, Object... args) {
        List<String> strings = message.getMessageAsStringList();
        if (strings.size() > 0) {
            TextComponent.Builder component = Component.text();
            strings.forEach(currentMessage -> {
                component.append(getComponent(getMessage(currentMessage, args)));
                component.append(Component.text("\n"));
            });
            return component.build();
        }
        return getComponent(getMessage(message.getMessageAsString(), args));
    }

    public Component getComponentMessage(String message, Object... args) {
        return getComponent(getMessage(message, args));
    }

    public Component getComponent(String message) {
        return Component.text(color(message));
    }

    protected String getMessage(Message message, Object... args) {
        return getMessage(String.join("\n", message.getMessageAsStringList()), args);
    }

    protected String getMessage(String message, Object... args) {

        List<Object> modifiedArgs = new ArrayList<>();
        for (Object arg : args) handleArg(arg, modifiedArgs);
        Object[] newArgs = modifiedArgs.toArray();

        return getString(message, newArgs);
    }

    private void handleArg(Object arg, List<Object> modifiedArgs) {
        if (arg instanceof Player player) {
            addPlayerDetails(modifiedArgs, player.getName(), player.getDisplayName());
        } else {
            modifiedArgs.add(arg);
        }
    }

    private void addPlayerDetails(List<Object> modifiedArgs, String name, String displayName) {
        modifiedArgs.add("%player%");
        modifiedArgs.add(name);
        modifiedArgs.add("%displayName%");
        modifiedArgs.add(displayName);
    }

    // ToDo, rework with component
    protected String getCenteredMessage(String message) {
        if (message == null || message.equals("")) return "";

        int CENTER_PX = 154;

        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }

    protected String color(String message) {
        if (message == null) return null;

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, String.valueOf(net.md_5.bungee.api.ChatColor.of(color)));
            matcher = pattern.matcher(message);
        }

        MiniMessage miniMessage = MiniMessage.builder().build();
        Component component = miniMessage.deserialize(message);
        message = LegacyComponentSerializer.legacyAmpersand().serialize(component);
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

}