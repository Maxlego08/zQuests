package fr.maxlego08.quests.api.storage.dto;

import java.util.Date;
import java.util.UUID;

public record CompletedQuestDTO(UUID unique_id, String name, Date completed_at, Date started_at, boolean is_favorite, long start_play_time, long complet_play_time) {
}
