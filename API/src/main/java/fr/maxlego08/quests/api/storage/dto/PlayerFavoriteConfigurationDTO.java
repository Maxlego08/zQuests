package fr.maxlego08.quests.api.storage.dto;

import fr.maxlego08.quests.api.utils.FavoritePlaceholderType;

import java.util.UUID;

public record PlayerFavoriteConfigurationDTO(UUID unique_id, int limit, FavoritePlaceholderType placeholder_type) {
}
