package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.Parameter;

import java.util.List;
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

    @Override
    public void increment() {
        if (this.isComplete()) {
            return;
        }
        this.amount++;
        if (this.isComplete()) {
            this.quest.onComplete(this.uniqueId);
        }
    }

    @Override
    public boolean hasParameters(Parameter<?>... parameters) {
        for (Parameter<?> parameter : parameters) {
            Object questParam = this.quest.getParameters().get(parameter.getKey());
            if (questParam == null || !matchesParameter(questParam, parameter.getValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesParameter(Object questParam, Object paramValue) {
        return questParam instanceof List<?> list ? list.contains(paramValue) : questParam.equals(paramValue);
    }
}
