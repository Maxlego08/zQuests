package fr.maxlego08.quests.api.storage.dto;

import fr.maxlego08.sarah.Column;

import java.util.UUID;

public record ActiveQuestDTO(@Column(value = "unique_id", primary = true) UUID unique_id,
                             @Column(value = "name", primary = true) String name,
                             @Column(value = "amount") long amount) {
}
