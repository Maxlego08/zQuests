package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.placeholder.LocalPlaceholder;
import fr.maxlego08.quests.save.Config;
import org.bukkit.entity.Player;

import java.util.Optional;

public class QuestPlaceholder {

    private QuestsPlugin plugin;
    private QuestManager questManager;

    public void register(QuestsPlugin plugin, QuestManager questManager) {

        this.plugin = plugin;
        this.questManager = questManager;

        LocalPlaceholder localPlaceholder = LocalPlaceholder.getInstance();

        localPlaceholder.register("name_", this::getQuestName);
        localPlaceholder.register("description_", this::getQuestDescription);

        localPlaceholder.register("thumbnail_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(quest -> quest.getThumbnail().name()).orElse("PAPER");
        });

        localPlaceholder.register("type_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(quest -> quest.getType().name()).orElse("Unknown");
        });

        localPlaceholder.register("objective_", (player, questId) -> String.valueOf(getQuestObjective(player, questId)));
        localPlaceholder.register("is_completed_", (player, questId) -> String.valueOf(getQuestIsCompleted(player, questId)));
        localPlaceholder.register("is_active_", (player, questId) -> String.valueOf(getQuestIsActive(player, questId)));
        localPlaceholder.register("progress_bar_", this::getProgressBar);
        localPlaceholder.register("progress_", (player, questId) -> String.valueOf(getProgress(player, questId)));

        localPlaceholder.register("lore_line_", (player, questId) -> {

            long progress = getProgress(player, questId);
            long goal = getQuestObjective(player, questId);

            boolean isCompleted = getQuestIsCompleted(player, questId);
            String line = isCompleted ? Config.loreLinePlaceholderComplete : Config.loreLinePlaceholderActive;
            progress = isCompleted ? goal : progress;

            line = line.replace("%goal%", String.valueOf(goal));
            line = line.replace("%progress%", String.valueOf(progress));
            line = line.replace("%progress-bar%", Config.progressBar.getProgressBar(progress, goal));

            return line;
        });
    }

    private long getProgress(Player player, String questId) {
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        return userQuest.findActive(questId).map(ActiveQuest::getAmount).orElse(0L);
    }

    private String getProgressBar(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        long goal = optional.map(Quest::getGoal).orElse(0L);

        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        long amount = userQuest.findActive(questId).map(ActiveQuest::getAmount).orElse(0L);

        return Config.progressBar.getProgressBar(amount, goal);
    }

    private String getQuestName(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::getName).orElse("Unknown");
    }

    private String getQuestDescription(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::getDescription).orElse("Unknown");
    }

    private boolean getQuestIsCompleted(Player player, String questId) {
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        return userQuest.isQuestCompleted(questId);
    }

    private boolean getQuestIsActive(Player player, String questId) {
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        return userQuest.isQuestActive(questId);
    }

    private long getQuestObjective(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::getGoal).orElse(0L);
    }
}
