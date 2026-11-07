package fr.maxlego08.quests.listeners;

import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.DialogClick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Listens to zMenu's {@code PlayerDialogClickEvent} for the {@link QuestType#DIALOG_CLICK} quests.
 * <p>
 * The event is registered by reflection, so zQuests still works with zMenu versions that do not have it.
 */
public final class DialogClickListener implements Listener {

    private static final String EVENT_CLASS = "fr.maxlego08.menu.api.event.events.PlayerDialogClickEvent";

    private DialogClickListener() {
    }

    /**
     * @return true when zMenu provides the event and the listener is registered
     */
    public static boolean register(Plugin plugin, QuestManager manager) {
        try {
            Class<? extends Event> eventClass = Class.forName(EVENT_CLASS).asSubclass(Event.class);
            Method getPlayer = eventClass.getMethod("getPlayer");
            Method getDialogName = eventClass.getMethod("getDialogName");
            Method getButtonName = eventClass.getMethod("getButtonName");

            Bukkit.getPluginManager().registerEvent(eventClass, new DialogClickListener(), EventPriority.MONITOR, (listener, event) -> {
                if (!eventClass.isInstance(event)) return;
                if (event instanceof Cancellable cancellable && cancellable.isCancelled()) return;
                try {
                    Player player = (Player) getPlayer.invoke(event);
                    if (player.hasMetadata("NPC")) return;
                    var click = new DialogClick((String) getDialogName.invoke(event), (String) getButtonName.invoke(event));
                    manager.handleQuests(player.getUniqueId(), QuestType.DIALOG_CLICK, 1, click);
                } catch (ReflectiveOperationException exception) {
                    plugin.getLogger().warning("Unable to read a dialog click: " + exception.getMessage());
                }
            }, plugin, true);
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException | ClassCastException exception) {
            return false;
        }
    }
}
