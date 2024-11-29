package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestType;

import java.util.Date;
import java.util.UUID;

public class ZActiveQuest implements ActiveQuest {

    private final UUID uniqueId;
    private final Quest quest;
    private long amount;

    public ZActiveQuest(UUID uniqueId, Quest quest, long amount) {
        this.uniqueId = uniqueId;
        this.quest = quest;
        this.amount = amount;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public Quest getQuest() {
        return this.quest;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public void addAmount(long amount) {
        this.amount += amount;
    }

    @Override
    public boolean isComplete() {
        return this.amount >= this.quest.getGoal();
    }

    @Override
    public boolean isType(QuestType type) {
        return this.quest.getType() == type;
    }

    @Override
    public boolean owningBy(UUID uniqueId) {
        return this.uniqueId.equals(uniqueId);
    }

    private boolean postCheck() {
        if (this.isComplete()) {
            this.quest.onComplete(this);
            return true;
        }

        return false;
    }

    @Override
    public boolean increment(int amount) {
        if (this.isComplete()) return false;

        this.amount += amount;
        return postCheck();
    }

    @Override
    public boolean incrementStatic(int amount) {
        if (this.isComplete()) return false;

        this.amount = amount;
        return postCheck();
    }

    @Override
    public CompletedQuest complete() {
        return new CompletedQuest(this.quest, new Date());
    }

    @Override
    public boolean isQuestAction(Object object) {
        return this.quest.getActions().stream().anyMatch(questAction -> questAction.isAction(object));
    }
}
