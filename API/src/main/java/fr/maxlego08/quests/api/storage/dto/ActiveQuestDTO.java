package fr.maxlego08.quests.api.storage.dto;

import java.util.Date;
import java.util.UUID;

public record ActiveQuestDTO(UUID unique_id, String name, long amount, boolean is_favorite, long start_play_time, Date created_at,
                             Date updated_at) {
}
