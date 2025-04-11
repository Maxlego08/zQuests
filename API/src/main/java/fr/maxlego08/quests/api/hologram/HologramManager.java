package fr.maxlego08.quests.api.hologram;

import fr.maxlego08.quests.api.event.QuestEvent;
import org.bukkit.event.Listener;

public interface HologramManager extends Listener {

    void onQuestEvent(QuestEvent event);

}
