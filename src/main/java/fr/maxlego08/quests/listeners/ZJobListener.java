package fr.maxlego08.quests.listeners;

import fr.maxlego08.jobs.api.JobManager;
import fr.maxlego08.jobs.api.event.events.JobLevelEvent;
import fr.maxlego08.jobs.api.event.events.JobPrestigeEvent;
import fr.maxlego08.jobs.api.players.PlayerJob;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.event.events.QuestStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class ZJobListener implements Listener {

    private final QuestsPlugin plugin;
    private final JobManager jobManager;
    private final QuestManager manager;

    public ZJobListener(QuestsPlugin plugin, QuestManager manager) {
        this.manager = manager;
        this.jobManager = plugin.getProvider(JobManager.class);
        this.plugin = plugin;
    }

    @EventHandler
    public void onJobLevel(JobLevelEvent event) {
        var player = event.getPlayer();
        this.manager.handleStaticQuests(player.getUniqueId(), QuestType.JOB_LEVEL, event.getLevel(), event.getJob());

        var prestigeAndLevel = (event.getPlayerJob().getPrestige() * 100) + event.getLevel();
        this.manager.handleStaticQuests(player.getUniqueId(), QuestType.JOB_PRESTIGE_AND_LEVEL, prestigeAndLevel, event.getJob());
    }

    @EventHandler
    public void onJobPrestige(JobPrestigeEvent event) {
        var player = event.getPlayer();
        this.manager.handleStaticQuests(player.getUniqueId(), QuestType.JOB_PRESTIGE, event.getPrestige(), event.getJob());
    }

    @EventHandler
    public void onQuestStart(QuestStartEvent event) {

        var activeQuest = event.getActiveQuest();
        var quest = activeQuest.getQuest();
        var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();

        if (!activeQuest.canComplete(event.getPlayerUUID(), fakeInventory)) return;

        var optional = jobManager.getPlayerJobs(event.getPlayerUUID());
        if (optional.isPresent()) {
            var playerJobs = optional.get();
            for (PlayerJob playerJob : new ArrayList<>(playerJobs.getJobs())) {

                var optionalJob = jobManager.getJob(playerJob.getJobId());
                if (optionalJob.isPresent() && activeQuest.isQuestAction(optionalJob.get())) {

                    int amount;
                    if (quest.getType() == QuestType.JOB_LEVEL) {
                        amount = playerJob.getLevel();
                    } else if (quest.getType() == QuestType.JOB_PRESTIGE) {
                        amount = playerJob.getPrestige();
                    } else if (quest.getType() == QuestType.JOB_PRESTIGE_AND_LEVEL) {
                        amount = (playerJob.getPrestige() * 100) + playerJob.getLevel();
                    } else {
                        continue;
                    }

                    if (activeQuest.incrementStatic(amount)) {
                        event.setCancelled(true);
                        manager.completeQuest(activeQuest);
                    }
                }
            }
        }
    }
}
