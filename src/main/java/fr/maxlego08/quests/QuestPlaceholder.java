package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.placeholder.LocalPlaceholder;
import fr.maxlego08.quests.save.Config;

import java.util.Optional;

public class QuestPlaceholder {

    public void register(QuestsPlugin plugin, QuestManager questManager) {

        LocalPlaceholder localPlaceholder = LocalPlaceholder.getInstance();

        localPlaceholder.register("quest_name_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(Quest::getDisplayName).orElse("Unknown");
        });

        localPlaceholder.register("quest_description_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(Quest::getDescription).orElse("Unknown");
        });

        localPlaceholder.register("quest_thumbnail_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(quest -> quest.getThumbnail().name()).orElse("PAPER");
        });

        localPlaceholder.register("quest_type_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(quest -> quest.getType().name()).orElse("Unknown");
        });

        localPlaceholder.register("quest_objective_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(Quest::getGoal).orElse(0L).toString();
        });

        localPlaceholder.register("quest_is_completed_", (player, questId) -> {
            UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
            return String.valueOf(userQuest.isQuestCompleted(questId));
        });

        localPlaceholder.register("quest_is_active_", (player, questId) -> {
            UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
            return String.valueOf(userQuest.isQuestActive(questId));
        });

        localPlaceholder.register("quest_progress_bar_", (player, questId) -> {

            Optional<Quest> optional = questManager.getQuest(questId);
            long goal = optional.map(Quest::getGoal).orElse(0L);

            UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
            long amount = userQuest.findActive(questId).map(ActiveQuest::getAmount).orElse(0L);

            return Config.progressBar.getProgressBar(amount, goal);
        });

        localPlaceholder.register("quest_progress_", (player, questId) -> {
            UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
            return userQuest.findActive(questId).map(ActiveQuest::getAmount).orElse(0L).toString();
        });
    }
}
