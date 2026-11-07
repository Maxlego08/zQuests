package fr.maxlego08.quests.listeners;

import fr.kazotaruu.economie.api.EconomieAPI;
import fr.kazotaruu.economie.api.event.BalanceChangeEvent;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EconomyNumberTracker implements Runnable, Listener {
    private final QuestManager manager;
    public EconomyNumberTracker(QuestManager manager) { this.manager = manager; }

    @Override
    public void run() {
        var api = Bukkit.getServicesManager().load(EconomieAPI.class);
        if (api == null) return;
        for (var player : Bukkit.getOnlinePlayers()) {
            if (player.hasMetadata("NPC")) continue;
            for (var currency : api.currencies()) {
                var balance = api.cachedBalance(player.getUniqueId(), currency.id());
                if (balance.isPresent()) NumberQuestProgress.update(manager, player.getUniqueId(), QuestType.ECO_NUMBER,
                        balance.getAsLong(), "balance:" + currency.id());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBalanceChange(BalanceChangeEvent event) {
        var player = Bukkit.getPlayer(event.getPlayer());
        if (player == null || player.hasMetadata("NPC")) return;
        // Process earnings before balance objectives: the same deposit must not advance a newly selected earnings quest.
        if (event.getAfter() > event.getBefore()) {
            long gain;
            try { gain = Math.subtractExact(event.getAfter(), event.getBefore()); }
            catch (ArithmeticException exception) { gain = Long.MAX_VALUE; }
            manager.handleQuests(event.getPlayer(), QuestType.ECO_NUMBER,
                    (int) Math.min(Integer.MAX_VALUE, gain), "earned:" + event.getCurrency().id());
        }
        NumberQuestProgress.update(manager, event.getPlayer(), QuestType.ECO_NUMBER,
                event.getAfter(), "balance:" + event.getCurrency().id());
    }
}
