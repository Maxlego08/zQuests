package fr.maxlego08.quests.api.storage.dto;

import java.util.UUID;

public record ActiveQuestDTO(UUID unique_id, String name, long amount) {
}
