package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

import java.util.Locale;

/**
 * CUSTOM_FISHING et SELL_FISH (plugin Peche) : filtre sur l'identifiant du poisson, sa rarete et son
 * palier. "*" (ou 0 pour le palier) accepte tout.
 */
public class PecheAction implements QuestAction {

    /** Ce que Peche envoie a chaque prise ou pile vendue. */
    public record Cible(String poisson, String rarete, int palier) {
        @Override
        public String toString() {
            return poisson;
        }
    }

    private final QuestType type;
    private final String poisson;
    private final String rarete;
    private final int palier;

    public PecheAction(QuestType type, String poisson, String rarete, int palier) {
        this.type = type;
        this.poisson = normaliser(poisson);
        this.rarete = normaliser(rarete);
        this.palier = Math.max(0, palier);
    }

    private static String normaliser(String value) {
        String clean = value == null ? "*" : value.trim().toLowerCase(Locale.ROOT);
        return clean.isEmpty() ? "*" : clean;
    }

    @Override
    public boolean isAction(Object target) {
        if (!(target instanceof Cible cible)) {
            return false;
        }
        return (poisson.equals("*") || poisson.equalsIgnoreCase(cible.poisson()))
                && (rarete.equals("*") || rarete.equalsIgnoreCase(cible.rarete()))
                && (palier == 0 || palier == cible.palier());
    }

    @Override
    public QuestType getQuestType() {
        return type;
    }
}
