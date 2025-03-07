package fr.maxlego08.quests.zcore.utils;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import org.bukkit.entity.Player;

public class QuestPlaceholderUtil {

    public static Placeholders createPlaceholder(QuestsPlugin plugin, Player player, Quest quest){
        Placeholders placeholders = new Placeholders();

        placeholders.register("quest-name", quest.getName());
        placeholders.register("quest-display-name", quest.getDisplayName());
        placeholders.register("quest-description", quest.getDescription());
        placeholders.register("quest-thumbnail", quest.getThumbnail().name());
        placeholders.register("quest-type", quest.getType().name());
        placeholders.register("quest-objective", String.valueOf(quest.getGoal()));
        placeholders.register("quest-lore-line", plugin.getQuestPlaceholder().getLoreLine(player, quest.getName()));
        placeholders.register("quest-progress-bar", plugin.getQuestPlaceholder().getProgressBar(player, quest.getName()));
        placeholders.register("quest-percent", plugin.getQuestPlaceholder().getPercent(player, quest.getName()));
        placeholders.register("quest-progress", String.valueOf(plugin.getQuestPlaceholder().getProgress(player, quest.getName())));

        return placeholders;
    }

}
