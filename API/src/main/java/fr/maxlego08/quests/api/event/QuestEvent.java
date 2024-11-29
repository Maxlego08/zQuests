package fr.maxlego08.quests.api.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestEvent extends Event {

    private final static HandlerList handlers = new HandlerList();

    /**
     *
     */
    public QuestEvent() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param isAsync
     */
    public QuestEvent(boolean isAsync) {
        super(isAsync);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the handlers
     */
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }

}
