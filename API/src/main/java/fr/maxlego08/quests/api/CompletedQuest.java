package fr.maxlego08.quests.api;

import java.util.Date;

public record CompletedQuest(Quest quest, Date completedAt) {
}
