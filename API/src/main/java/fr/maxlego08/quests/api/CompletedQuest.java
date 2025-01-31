package fr.maxlego08.quests.api;

import java.util.Date;

/**
 * Represents a quest that has been completed by a user.
 *
 * @param quest  the quest that has been completed
 * @param completedAt  the date when the quest has been completed
 */
public record CompletedQuest(Quest quest, Date completedAt) {
}