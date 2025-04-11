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

import java.util.Locale;

public record HologramConfiguration(
        Location location,
        String text,
        Display.Billboard billboard,
        Vector3f scale,
        Vector3f translation,
        Display.Brightness brightness,
        float shadowRadius,
        float shadowStrength,
        int visibilityDistance,
        TextColor background,
        TextDisplay.TextAlignment textAlignment,
        boolean textShadow,
        boolean seeThrough
) {

    public static HologramConfiguration fromConfiguration(TypedMapAccessor accessor) {

        return new HologramConfiguration(
                configureLocation(accessor),
                accessor.getString("text", "Hummm, you need to add a text !"),
                Display.Billboard.valueOf(accessor.getString("billboard", Display.Billboard.CENTER.name()).toUpperCase()),
                new Vector3f( //
                        accessor.getFloat("scale-x", 1), //
                        accessor.getFloat("scale-y", 1), //
                        accessor.getFloat("scale-z", 1) //
                ), //
                new Vector3f( //
                        accessor.getFloat("translation-x", 0), //
                        accessor.getFloat("translation-y", 0), //
                        accessor.getFloat("translation-z", 0) //
                ),
                new Display.Brightness(
                        accessor.getInt("brightness-block", 15),
                        accessor.getInt("brightness-sky", 15)
                ),
                accessor.getFloat("shadow-radius", 0),
                accessor.getFloat("shadow-strength", 1.0f),
                accessor.getInt("visibility-distance", -1),
                configureBackground(accessor),
                TextDisplay.TextAlignment.valueOf(accessor.getString("text-alignment", TextDisplay.TextAlignment.CENTER.name()).toUpperCase()),
                accessor.getBoolean("text-shadow", false),
                accessor.getBoolean("see-through", false)
        );
    }

    private static Location configureLocation(TypedMapAccessor accessor) {
        String[] locationArray = accessor.getString("location").split(",");
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
