package fr.maxlego08.quests.listeners;

import fr.kazotaruu.peche.api.event.PoissonPecheEvent;
import fr.kazotaruu.peche.api.event.PoissonsVendusEvent;
import fr.maxlego08.quests.actions.PecheAction;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/** Les quetes du plugin Peche : CUSTOM_FISHING (poissons pris) et SELL_FISH (poissons vendus). */
public class PecheListener implements Listener {

    private final QuestManager manager;

    public PecheListener(QuestManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPeche(PoissonPecheEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        manager.handleQuests(event.getPlayer().getUniqueId(), QuestType.CUSTOM_FISHING, 1,
                new PecheAction.Cible(event.getPoisson(), event.getRarete(), event.getPalier()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVente(PoissonsVendusEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        for (PoissonsVendusEvent.Vente vente : event.getVentes()) {
            manager.handleQuests(event.getPlayer().getUniqueId(), QuestType.SELL_FISH, vente.quantite(),
                    new PecheAction.Cible(vente.poisson(), vente.rarete(), vente.palier()));
        }
    }
}
