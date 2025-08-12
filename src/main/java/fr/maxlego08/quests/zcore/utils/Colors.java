package fr.maxlego08.quests.zcore.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class Colors {
    private Colors() {}

    // Supported color names from java.awt.Color
    private static final Map<String, Color> NAMED = new HashMap<>();
    static {
        NAMED.put("white", Color.WHITE);
        NAMED.put("black", Color.BLACK);
        NAMED.put("red", Color.RED);
        NAMED.put("green", Color.GREEN);
        NAMED.put("blue", Color.BLUE);
        NAMED.put("cyan", Color.CYAN);
        NAMED.put("magenta", Color.MAGENTA);
        NAMED.put("yellow", Color.YELLOW);
        NAMED.put("gray", Color.GRAY);
        NAMED.put("grey", Color.GRAY); // alias
        NAMED.put("darkgray", Color.DARK_GRAY);
        NAMED.put("darkgrey", Color.DARK_GRAY); // alias
        NAMED.put("lightgray", Color.LIGHT_GRAY);
        NAMED.put("lightgrey", Color.LIGHT_GRAY); // alias
        NAMED.put("pink", Color.PINK);
        NAMED.put("orange", Color.ORANGE);
    }

    /**
     * Parses a color from a string input.
     * Supported formats:
     *  - Named colors (e.g. "red", "lightGray", "dark-grey", case-insensitive)
     *  - Hexadecimal: "#RGB", "#RRGGBB", "#AARRGGBB"
     *  - RGB or RGBA: "r,g,b" or "r,g,b,a" (values 0-255)
     *  - Decimal integer: RGB (0..16777215) or ARGB (signed 32-bit integer)
     *
     * @param input the string representing the color
     * @return the parsed Color object
     * @throws IllegalArgumentException if the format is invalid or unsupported
     */
    public static Color parseColor(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Color string cannot be null.");
        }
        String s = input.trim();

        // 0) Check named colors
        Color named = NAMED.get(normalizeName(s));
        if (named != null) return named;

        // 1) Hexadecimal starting with '#'
        if (s.startsWith("#")) {
            String hex = s.substring(1).trim();
            if (hex.length() == 3) {
                // #RGB -> #RRGGBB
                hex = "" + hex.charAt(0) + hex.charAt(0)
                        + hex.charAt(1) + hex.charAt(1)
                        + hex.charAt(2) + hex.charAt(2);
            }
            try {
                if (hex.length() == 6) {
                    int rgb = Integer.parseInt(hex, 16);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    return new Color(r, g, b);
                } else if (hex.length() == 8) {
                    // Interpret as AARRGGBB
                    long argb = Long.parseLong(hex, 16);
                    int a = (int) ((argb >> 24) & 0xFF);
                    int r = (int) ((argb >> 16) & 0xFF);
                    int g = (int) ((argb >> 8) & 0xFF);
                    int b = (int) (argb & 0xFF);
                    return new Color(r, g, b, a);
                }
            } catch (NumberFormatException ignored) {}
            throw new IllegalArgumentException("Invalid hex format. Use #RGB, #RRGGBB, or #AARRGGBB.");
        }

        // 2) CSV format: "r,g,b" or "r,g,b,a"
        if (s.contains(",")) {
            String[] parts = s.split(",");
            if (parts.length == 3 || parts.length == 4) {
                try {
                    int r = parse255(parts[0]);
                    int g = parse255(parts[1]);
                    int b = parse255(parts[2]);
                    if (parts.length == 4) {
                        int a = parse255(parts[3]);
                        return new Color(r, g, b, a);
                    } else {
                        return new Color(r, g, b);
                    }
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid RGB(A) values. Expected integers 0..255.", ex);
                }
            } else {
                throw new IllegalArgumentException("Invalid RGB format. Use \"r,g,b\" or \"r,g,b,a\".");
            }
        }

        // 3) Decimal integer: RGB 24-bit or ARGB 32-bit
        try {
            long val = Long.parseLong(s);
            if (val >= 0 && val <= 0xFFFFFFL) {
                // Treat as RGB (alpha = 255)
                return new Color((int) val);
            }
            // Treat as ARGB
            int intVal = (int) val;
            return new Color(intVal, true);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Unsupported color format. Use a name (e.g., red), #hex, \"r,g,b\", or a decimal RGB/ARGB integer."
            );
        }
    }

    /**
     * Normalizes a color name: lowercases it and removes spaces, hyphens, and underscores.
     */
    private static String normalizeName(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[\\s_-]", "");
    }

    /**
     * Parses an integer between 0 and 255 (inclusive).
     */
    private static int parse255(String s) {
        int v = Integer.parseInt(s.trim());
        if (v < 0 || v > 255) {
            throw new NumberFormatException("Value out of range 0..255: " + v);
        }
        return v;
    }
}
