package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.UserQuest;
import java.util.Comparator;
import java.util.Optional;

/** Selection of the active quest shown by the HUD placeholders for each quest type. */
public final class SequentialQuest {
    private SequentialQuest() {}

    /** Oldest active quest of the given category (principal, secondaire or journalier). */
    public static Optional<ActiveQuest> current(QuestManager manager, UserQuest user, String category) {
        if (user == null) return Optional.empty();
        return user.getActiveQuests().stream()
            .filter(q -> manager.getQuestCategory(q.getQuest().getName()).map(category::equals).orElse(false))
            .min(Comparator.comparing(ActiveQuest::getCreatedAt)
                .thenComparing(q -> q.getQuest().getName()));
    }
}
