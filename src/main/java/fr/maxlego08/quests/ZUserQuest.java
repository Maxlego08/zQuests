package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.UserQuest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZUserQuest implements UserQuest {

    private final List<ActiveQuest> activeQuests;
    private final List<CompletedQuest> completedQuests;

    public ZUserQuest() {
        this.activeQuests = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
    }

    public ZUserQuest(List<ActiveQuest> activeQuests, List<CompletedQuest> completedQuests) {
        this.activeQuests = activeQuests;
        this.completedQuests = completedQuests;
    }

    @Override
    public List<ActiveQuest> getActiveQuests() {
        return this.activeQuests;
    }

    @Override
    public List<CompletedQuest> getCompletedQuests() {
        return this.completedQuests;
    }

    @Override
    public boolean isQuestActive(Quest quest) {
        return this.activeQuests.stream().anyMatch(activeQuest -> activeQuest.getQuest().equals(quest));
    }

    @Override
    public boolean isQuestCompleted(Quest quest) {
        return this.completedQuests.stream().anyMatch(completedQuest -> completedQuest.quest().equals(quest));
    }

    @Override
    public boolean isQuestActive(String questName) {
        return this.activeQuests.stream().anyMatch(activeQuest -> activeQuest.getQuest().getName().equals(questName));
    }

    @Override
    public boolean isQuestCompleted(String questName) {
        return this.completedQuests.stream().anyMatch(completedQuest -> completedQuest.quest().getName().equalsIgnoreCase(questName));
    }

    @Override
    public boolean canStartQuest(Quest quest) {
        return !isQuestActive(quest) && !isQuestCompleted(quest);
    }

    @Override
    public Optional<ActiveQuest> findActive(String questName) {
        return this.activeQuests.stream().filter(activeQuest -> activeQuest.getQuest().getName().equalsIgnoreCase(questName)).findFirst();
    }
}
