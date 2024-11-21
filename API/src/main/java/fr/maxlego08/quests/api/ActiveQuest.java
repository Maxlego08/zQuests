package fr.maxlego08.quests.api;

import java.util.UUID;

public interface ActiveQuest {

    UUID getUniqueId();

    Quest getQuest();

    long getAmount();

    void addAmount(long amount);

    void setAmount(long amount);

    boolean isComplete();

    boolean isType(QuestType type);

    boolean owningBy(UUID uniqueId);

    boolean increment(int amount);

    CompletedQuest complete();

    boolean isQuestAction(Object object);
}
