package fr.maxlego08.quests.zcore.utils;

import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.messages.BossBarAnimation;
import fr.maxlego08.quests.messages.Message;
import fr.maxlego08.quests.messages.MessageType;
import fr.maxlego08.quests.messages.types.BossBarMessage;
import fr.maxlego08.quests.messages.types.ClassicMessage;
import fr.maxlego08.quests.messages.types.TitleMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageUtils extends LocationUtils {

    private String getString(String message, Object[] newArgs) {
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

    protected void message(CommandSender sender, Message message, Object... args) {

        QuestsPlugin plugin = JavaPlugin.getPlugin(QuestsPlugin.class);
        MetaUpdater updater = plugin.getInventoryManager().getMeta();

        if (sender instanceof Player player) {
            message.getMessages().forEach(essentialsMessage -> {

                if (essentialsMessage instanceof ClassicMessage classicMessage) {

                    switch (essentialsMessage.messageType()) {
                        case TCHAT, WITHOUT_PREFIX -> sendTchatMessage(updater, sender, classicMessage, args);
                        case ACTION -> classicMessage.messages().forEach(currentMessage -> {
                            updater.sendAction(player, getMessage(currentMessage, args));
                        });
                        case CENTER -> classicMessage.messages().forEach(currentMessage -> {
                            updater.sendMessage(player, getCenteredMessage(getMessage(currentMessage, args)));
                        });
                    }

                } else if (essentialsMessage instanceof BossBarMessage bossBarMessage) {

                    BossBarAnimation.from(plugin, player, getMessage(papi(bossBarMessage.text(), player)), 1f, bossBarMessage.getColor(), bossBarMessage.getOverlay(), bossBarMessage.getFlags(), bossBarMessage.duration());

                } else if (essentialsMessage instanceof TitleMessage titleMessage) {

                    String title = getMessage(papi(getMessage(titleMessage.title(), args), player));
                    String subtitle = getMessage(papi(getMessage(titleMessage.subtitle(), args), player));
                    updater.sendTitle(player, title, subtitle, titleMessage.start(), titleMessage.time(), titleMessage.end());
                }
            });
        } else {
            message.getMessages().forEach(essentialsMessage -> {
                if (essentialsMessage instanceof ClassicMessage classicMessage) {
                    sendTchatMessage(updater, sender, classicMessage, args);
                }
            });
        }
    }

    private void sendTchatMessage(MetaUpdater updater, CommandSender sender, ClassicMessage classicMessage, Object... args) {
        boolean isWithoutPrefix = classicMessage.messageType() == MessageType.WITHOUT_PREFIX || classicMessage.messages().size() > 1;
        classicMessage.messages().forEach(message -> updater.sendMessage(sender, (isWithoutPrefix ? "" : Message.PREFIX.getMessageAsString()) + getMessage(message, args)));
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

        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

}