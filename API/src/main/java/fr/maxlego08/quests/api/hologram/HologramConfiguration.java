package fr.maxlego08.quests.api.hologram;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public record HologramConfiguration(Location location, List<String> texts, Display.Billboard billboard, Vector3f scale,
                                    Vector3f translation, Display.Brightness brightness, float shadowRadius,
                                    float shadowStrength, int visibilityDistance, TextColor background,
                                    TextDisplay.TextAlignment textAlignment, boolean textShadow, boolean seeThrough) {
    
    public static List<HologramConfiguration> fromConfiguration(TypedMapAccessor accessor) {

        var text = getTexts(accessor);
        var billboard = Display.Billboard.valueOf(accessor.getString("billboard", Display.Billboard.CENTER.name()).toUpperCase());
        var scale = configureScale(accessor);
        var translation = new Vector3f(accessor.getFloat("translation-x", 0), accessor.getFloat("translation-y", 0), accessor.getFloat("translation-z", 0));
        var brightness = new Display.Brightness(accessor.getInt("brightness-block", 15), accessor.getInt("brightness-sky", 15));
        var shadowRadius = accessor.getFloat("shadow-radius", 0);
        var shadowStrength = accessor.getFloat("shadow-strength", 1.0f);
        var visibilityDistance = accessor.getInt("visibility-distance", -1);
        var background = configureBackground(accessor);
        var textAlignment = TextDisplay.TextAlignment.valueOf(accessor.getString("text-alignment", TextDisplay.TextAlignment.CENTER.name()).toUpperCase());
        var textShadow = accessor.getBoolean("text-shadow", false);
        var seeThrough = accessor.getBoolean("see-through", false);

        return configureLocations(accessor).stream().map(location -> new HologramConfiguration(location, text, billboard, scale, translation, brightness, shadowRadius, shadowStrength, visibilityDistance, background, textAlignment, textShadow, seeThrough)).toList();
    }

    private static List<String> getTexts(TypedMapAccessor accessor) {

        if (accessor.contains("text")) {
            return List.of(accessor.getString("text"));
        } else if (accessor.contains("texts")) {
            return accessor.getStringList("texts");
        }
        return List.of("Hummm, you need to add a text !");
    }

    private static List<Location> configureLocations(TypedMapAccessor accessor) {

        if (accessor.contains("locations")) {
            return accessor.getStringList("locations").stream().map(HologramConfiguration::stringToLocation).toList();
        } else if (accessor.contains("location")) {
            return List.of(stringToLocation(accessor.getString("location")));
        } else return new ArrayList<>();
    }

    private static Vector3f configureScale(TypedMapAccessor accessor) {

        if (accessor.contains("scale")) {
            String scaleStr = accessor.getString("scale", null);
            if (scaleStr != null) {
                String[] values = scaleStr.replace(" ", "").split(",");
                try {
                    if (values.length == 3) {
                        return new Vector3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
                    }
                    float value = Float.parseFloat(values[0]);
                    return new Vector3f(value, value, value);
                } catch (NumberFormatException ignored) {
                }
            }

            float value = accessor.getFloat("scale", 1);
            return new Vector3f(value, value, value);
        }

        return new Vector3f(accessor.getFloat("scale-x", 1), accessor.getFloat("scale-y", 1), accessor.getFloat("scale-z", 1));
    }

    private static Location stringToLocation(String location) {
        String[] locationArray = location.split(",");
        World w = Bukkit.getServer().getWorld(locationArray[0]);
        float x = Float.parseFloat(locationArray[1]);
        float y = Float.parseFloat(locationArray[2]);
        float z = Float.parseFloat(locationArray[3]);
        if (locationArray.length == 6) {
            float yaw = Float.parseFloat(locationArray[4]);
            float pitch = Float.parseFloat(locationArray[5]);
            return new Location(w, x, y, z, yaw, pitch);
        }
        return new Location(w, x, y, z);
    }

    private static TextColor configureBackground(TypedMapAccessor accessor) {
        String backgroundStr = accessor.getString("text-background", null);
        if (backgroundStr == null || backgroundStr.equalsIgnoreCase("default")) {
            return null;
        } else if (backgroundStr.equalsIgnoreCase("transparent")) {
            return () -> 0;
        } else if (backgroundStr.startsWith("#")) {
            return TextColor.fromHexString(backgroundStr);
        } else {
            String formattedName = backgroundStr.toLowerCase(Locale.ROOT).trim().replace(' ', '_');
            return NamedTextColor.NAMES.value(formattedName);
        }
    }

}
