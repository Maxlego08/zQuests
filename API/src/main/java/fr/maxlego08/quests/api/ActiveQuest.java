package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.utils.Parameter;

import java.util.UUID;

public interface ActiveQuest {

    UUID getUniqueId();

    Quest getQuest();

    long getAmount();

    void addAmount(long amount);

    boolean isComplete();

    boolean isType(QuestType type);

    boolean owningBy(UUID uniqueId);

    boolean increment(int amount);

    boolean hasParameters(Parameter<?>... parameters);

    CompletedQuest complete();
}
