package fr.maxlego08.quests.listeners;

import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import java.util.UUID;

final class NumberQuestProgress {
    private NumberQuestProgress() {}

    static void update(QuestManager manager, UUID player, QuestType type, long amount, String key) {
        int value = (int) Math.max(0, Math.min(Integer.MAX_VALUE, amount));
        var user = manager.getUserQuest(player);
        if (user == null) return;
        boolean changed = user.getActiveQuests().stream().anyMatch(active ->
                active.getQuest().getType() == type && !active.isComplete() && active.isQuestAction(key)
                        && active.canComplete() && (active.getAmount() != value || value >= active.getQuest().getGoal()));
        if (changed) manager.handleStaticQuests(player, type, value, key);
    }
}
