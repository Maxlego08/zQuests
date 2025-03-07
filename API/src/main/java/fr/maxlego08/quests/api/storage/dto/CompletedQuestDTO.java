package fr.maxlego08.quests.api.storage.dto;

import java.util.Date;
import java.util.UUID;

public record CompletedQuestDTO(UUID unique_id, String name, Date completed_at, Date started_at) {
}
