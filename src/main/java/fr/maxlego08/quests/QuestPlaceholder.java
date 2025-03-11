package fr.maxlego08.quests;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestsGroup;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.placeholder.LocalPlaceholder;
import fr.maxlego08.quests.save.Config;
import fr.maxlego08.quests.zcore.utils.QuestPlaceholderUtil;
import fr.maxlego08.quests.zcore.utils.ZUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class QuestPlaceholder extends ZUtils {

    public QuestsPlugin plugin;
    public QuestManager questManager;

    public void register(QuestsPlugin plugin, QuestManager questManager) {

        this.plugin = plugin;
        this.questManager = questManager;

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();

        placeholder.register("name_", this::getQuestName);
        placeholder.register("description_", this::getQuestDescription);
        placeholder.register("model_id_", (p, q) -> String.valueOf(this.getQuestModelId(p, q)));
        placeholder.register("is_favorite_", (p, q) -> String.valueOf(this.isFavorite(p, q)));
        placeholder.register("can_change_favorite_", (p, q) -> String.valueOf(this.canChangeFavorite(p, q)));

        placeholder.register("thumbnail_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(quest -> quest.getThumbnail().name()).orElse("PAPER");
        });

        placeholder.register("type_", (player, questId) -> {
            Optional<Quest> optional = questManager.getQuest(questId);
            return optional.map(quest -> quest.getType().name()).orElse("Unknown");
        });

        placeholder.register("objective_", (player, questId) -> String.valueOf(getQuestObjective(player, questId)));
        placeholder.register("is_completed_", (player, questId) -> String.valueOf(getQuestIsCompleted(player, questId)));
        placeholder.register("is_active_", (player, questId) -> String.valueOf(getQuestIsActive(player, questId)));
        placeholder.register("progress_bar_", this::getProgressBar);
        placeholder.register("percent_", this::getPercent);
        placeholder.register("progress_", (player, questId) -> String.valueOf(getProgress(player, questId)));
        placeholder.register("lore_line_", this::getLoreLine);

        placeholder.register("group_name_", (player, groupKey) -> questManager.getGroup(groupKey).map(QuestsGroup::getDisplayName).orElse("Quests group " + groupKey + " was not found"));
        placeholder.register("group_count_", (player, groupKey) -> String.valueOf(questManager.getGroup(groupKey).map(QuestsGroup::getQuests).map(List::size).orElse(0)));
        placeholder.register("group_finish_", (player, groupKey) -> String.valueOf(getCompletedQuestsCount(player, groupKey)));

        placeholder.register("group_percent_", (player, groupKey) -> {
            long completedQuestsCount = getCompletedQuestsCount(player, groupKey);
            long totalQuestsCount = questManager.getGroup(groupKey).map(QuestsGroup::getQuests).orElse(new ArrayList<>()).size();
            return format(percent(completedQuestsCount, totalQuestsCount));
        });

        placeholder.register("group_total_percent_", (player, groupKey) -> {

            var user = questManager.getUserQuest(player.getUniqueId());
            var activeQuests = user.getActiveQuests();

            var groupQuests = questManager.getGroup(groupKey).map(QuestsGroup::getQuests).orElse(new ArrayList<>());
            if (groupQuests.isEmpty()) return "0";

            int totalQuests = groupQuests.size();
            var completedQuests = user.getCompletedQuests().stream().filter(completedQuest -> groupQuests.contains(completedQuest.quest())).count();
            double completedProgress = completedQuests * 100;

            var inProgressSum = activeQuests.stream().filter(completedQuest -> groupQuests.contains(completedQuest.getQuest())).mapToDouble(q -> getPercent(player, q.getQuest())).average().orElse(0.0);

            return format((completedProgress + inProgressSum) / totalQuests);
        });


        placeholder.register("favorite_quests", player -> {
            var user = questManager.getUserQuest(player.getUniqueId());
            var favoriteQuests = user.getFavoriteQuests().stream().sorted(Comparator.comparing(ActiveQuest::getCreatedAt)).limit(Config.placeholderFavorite.limit()).toList();
            if (favoriteQuests.isEmpty()) return Config.placeholderFavorite.empty();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < favoriteQuests.size(); i++) {
                ActiveQuest favoriteQuest = favoriteQuests.get(i);
                Placeholders placeholders = QuestPlaceholderUtil.createPlaceholder(plugin, player, favoriteQuest.getQuest());
                builder.append(placeholders.parse(Config.placeholderFavorite.result()));

                if (i < favoriteQuests.size() - 1) {
                    builder.append(placeholders.parse(Config.placeholderFavorite.between()));
                }
            }

            return builder.toString();
        });
    }

    public long getProgress(Player player, String questId) {
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        return userQuest.findActive(questId).map(ActiveQuest::getAmount).orElse(0L);
    }

    public String getPercent(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(q -> format(getPercent(player, q))).orElse("0");
    }

    public String getProgressBar(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        long goal = optional.map(Quest::getGoal).orElse(0L);

        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        long amount = userQuest.findActive(questId).map(ActiveQuest::getAmount).orElse(0L);

        return Config.progressBar.getProgressBar(amount, goal);
    }

    public double getPercent(Player player, Quest quest) {
        long goal = quest.getGoal();

        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        long amount = userQuest.findActive(quest.getName()).map(ActiveQuest::getAmount).orElse(0L);

        return percent(amount, goal);
    }

    public String getQuestName(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::getName).orElse("Unknown");
    }

    public String getQuestDescription(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::getDescription).orElse("Unknown");
    }

    public int getQuestModelId(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::getCustomModelId).orElse(0);
    }

    public boolean canChangeFavorite(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::canChangeFavorite).orElse(false);
    }

    public boolean isFavorite(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::isFavorite).orElse(false);
    }

    public boolean getQuestIsCompleted(Player player, String questId) {
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        return userQuest.isQuestCompleted(questId);
    }

    public boolean getQuestIsActive(Player player, String questId) {
        UserQuest userQuest = questManager.getUserQuest(player.getUniqueId());
        return userQuest.isQuestActive(questId);
    }

    public long getQuestObjective(Player player, String questId) {
        Optional<Quest> optional = questManager.getQuest(questId);
        return optional.map(Quest::getGoal).orElse(0L);
    }

    public long getCompletedQuestsCount(Player player, String groupKey) {
        var user = questManager.getUserQuest(player.getUniqueId());
        var completedQuests = user.getCompletedQuests();
        var groupQuests = questManager.getGroup(groupKey).map(QuestsGroup::getQuests).orElse(new ArrayList<>());
        if (groupQuests.isEmpty()) return 0;

        return completedQuests.stream().filter(completedQuest -> groupQuests.contains(completedQuest.quest())).count();
    }

    public String getLoreLine(Player player, String questId) {
        long progress = getProgress(player, questId);
        long goal = getQuestObjective(player, questId);

        boolean isCompleted = getQuestIsCompleted(player, questId);
        String line = isCompleted ? Config.loreLinePlaceholderComplete : Config.loreLinePlaceholderActive;
        progress = isCompleted ? goal : progress;

        line = line.replace("%goal%", String.valueOf(goal));
        line = line.replace("%progress%", String.valueOf(progress));
        line = line.replace("%progress-bar%", Config.progressBar.getProgressBar(progress, goal));

        return line;
    }
}
