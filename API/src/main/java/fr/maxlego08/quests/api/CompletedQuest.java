package fr.maxlego08.quests.api;

import java.util.Date;

/**
 * Represents a quest that has been completed by a user.
 *
 * @param quest       the quest that has been completed
 * @param completedAt the date when the quest has been completed
 *                    (defaults to the current date)
 * @param startedAt   the date when the quest was started (defaults to the
 *                    current date)
 * @param isFavorite  whether the quest is marked as favorite
 */
public record CompletedQuest(Quest quest, Date completedAt, Date startedAt, boolean isFavorite) {
}