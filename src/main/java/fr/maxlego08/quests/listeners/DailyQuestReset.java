package fr.maxlego08.quests.listeners;

import fr.maxlego08.quests.api.QuestManager;
import org.bukkit.Bukkit;

import java.time.LocalDate;

/** Resets the daily quests of online players when the server date changes (00:00). Offline players are reset on join. */
public class DailyQuestReset implements Runnable {
    private final QuestManager manager;
    private LocalDate currentDay = LocalDate.now();

    public DailyQuestReset(QuestManager manager) { this.manager = manager; }

    @Override
    public void run() {
        var today = LocalDate.now();
        if (today.equals(this.currentDay)) return;
        this.currentDay = today;
        for (var player : Bukkit.getOnlinePlayers()) {
            this.manager.resetDailyQuests(player.getUniqueId());
        }
    }
}
