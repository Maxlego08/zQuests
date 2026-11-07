package fr.maxlego08.quests.zcore.utils;

import fr.maxlego08.quests.save.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Builds a human-readable list of rewards from the raw "rewards" section of a quest.
 * <p>
 * Only commands that give something are described:
 * <ul>
 *     <li>{@code give <player> <item> [amount]} → {@code 5x Diamond}</li>
 *     <li>{@code eco give <player> <amount>} (EssentialsX style) → {@code 500 $}</li>
 *     <li>{@code eco give <currency> <amount> <targets>} → {@code 500 coins}</li>
 * </ul>
 * Every other action (messages, unknown commands, ...) is ignored.
 */
public final class RewardsDisplay {

    private RewardsDisplay() {
    }

    public static List<String> fromRawRewards(List<?> rawRewards) {
        if (rawRewards == null || rawRewards.isEmpty()) return Collections.emptyList();

        List<String> lines = new ArrayList<>();
        for (Object element : rawRewards) {
            if (!(element instanceof Map<?, ?> map)) continue;

            Object type = map.get("type");
            if (type != null && !type.toString().toLowerCase(Locale.ROOT).contains("command")) continue;

            Object commands = map.get("commands");
            if (commands instanceof List<?> list) {
                for (Object command : list) {
                    String line = describeCommand(String.valueOf(command));
                    if (line != null) lines.add(line);
                }
            }
        }
        return lines;
    }

    public static String describeCommand(String command) {
        if (command == null) return null;
        String[] args = command.trim().replaceFirst("^/", "").split("\\s+");
        if (args.length == 0) return null;

        String label = args[0].toLowerCase(Locale.ROOT);
        int colon = label.indexOf(':');
        if (colon >= 0) label = label.substring(colon + 1);

        return switch (label) {
            case "give", "i", "item" -> describeGive(args);
            case "eco", "economy", "economie", "money" -> describeEco(args);
            default -> null;
        };
    }

    // give <player> <item> [amount]
    private static String describeGive(String[] args) {
        if (args.length < 3) return null;
        String amount = args.length >= 4 && isNumber(args[3]) ? args[3] : "1";
        return Config.rewardsDisplayItem.replace("%amount%", amount).replace("%item%", formatItem(args[2]));
    }

    // eco give <player> <amount> | eco give <currency> <amount> <targets>
    private static String describeEco(String[] args) {
        if (args.length < 4 || !args[1].equalsIgnoreCase("give")) return null;

        int amountIndex = -1;
        for (int i = 2; i < args.length; i++) {
            if (isNumber(args[i])) {
                amountIndex = i;
                break;
            }
        }
        if (amountIndex < 0) return null;

        String currency = Config.rewardsDisplayDefaultCurrency;
        // A non placeholder token before the amount is a currency name (eco give <currency> <amount> <targets>)
        if (amountIndex == 3 && !args[2].contains("%")) {
            // EssentialsX style "eco give <player> <amount>" has nothing after the amount
            if (args.length > 4) currency = args[2];
        }

        return Config.rewardsDisplayMoney.replace("%amount%", args[amountIndex]).replace("%currency%", currency).trim();
    }

    private static String formatItem(String raw) {
        String item = raw;
        int bracket = indexOfAny(item, '[', '{');
        if (bracket >= 0) item = item.substring(0, bracket);
        int colon = item.indexOf(':');
        if (colon >= 0) item = item.substring(colon + 1);

        StringBuilder builder = new StringBuilder();
        for (String word : item.toLowerCase(Locale.ROOT).split("_")) {
            if (word.isEmpty()) continue;
            if (!builder.isEmpty()) builder.append(' ');
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return builder.toString();
    }

    private static int indexOfAny(String value, char a, char b) {
        int ia = value.indexOf(a);
        int ib = value.indexOf(b);
        if (ia < 0) return ib;
        if (ib < 0) return ia;
        return Math.min(ia, ib);
    }

    private static boolean isNumber(String value) {
        return value.matches("\\d+(\\.\\d+)?");
    }
}
