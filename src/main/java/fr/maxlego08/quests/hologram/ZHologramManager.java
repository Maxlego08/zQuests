package fr.maxlego08.quests.hologram;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.QuestEvent;
import fr.maxlego08.quests.api.event.events.QuestStartEvent;
import fr.maxlego08.quests.api.event.events.QuestUserLoadEvent;
import fr.maxlego08.quests.api.hologram.HologramManager;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ZHologramManager implements HologramManager {

    private final QuestsPlugin plugin;

    public ZHologramManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onQuestEvent(QuestEvent event) {

        this.plugin.getLogger().info("Receive event " + event);

        if (event instanceof QuestStartEvent startEvent) {
            this.onQuestStart(startEvent, startEvent.getActiveQuest(), startEvent.getPlayerUUID());
        } else if (event instanceof QuestUserLoadEvent userLoadEvent) {
            this.onQuestLoad(userLoadEvent, userLoadEvent.getUserQuest());
        }
    }

    private void onQuestLoad(QuestUserLoadEvent userLoadEvent, UserQuest userQuest) {

    }

    private void onQuestStart(QuestStartEvent startEvent, ActiveQuest activeQuest, UUID playerUUID) {

        var player = Bukkit.getPlayer(playerUUID);
        System.out.println("A " + player);
        if (player == null) return;

        var quest = activeQuest.getQuest();
        System.out.println("B " + quest);
        if (!quest.hasHologram()) return;

        var userQuest = this.plugin.getQuestManager().getUserQuest(playerUUID);

        var questHologram = new EssentialsHologram(this.plugin, quest);
        questHologram.create(player);

        userQuest.addHologram(questHologram);
        System.out.println("NORMALEMENT OUI");
    }
}
