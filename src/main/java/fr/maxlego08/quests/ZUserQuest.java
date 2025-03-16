package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.UserQuest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ZUserQuest implements UserQuest {

    private final List<ActiveQuest> activeQuests;
    private final List<CompletedQuest> completedQuests;
    private String currentGroup;
    private boolean isExtend;

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
    public List<ActiveQuest> getSortActiveQuests() {
        return this.activeQuests.stream().sorted(Comparator.comparingInt(q -> q.getQuest().isUnique() ? 0 : 1)).toList();
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

    @Override
    public Optional<ActiveQuest> findActive(Quest quest) {
        return findActive(quest.getName());
    }

    @Override
    public void removeActiveQuest(ActiveQuest activeQuest) {
        this.activeQuests.remove(activeQuest);
    }

    @Override
    public List<ActiveQuest> getFavoriteQuests() {
        return this.activeQuests.stream().filter(ActiveQuest::isFavorite).toList();
    }

    @Override
    public Optional<CompletedQuest> findComplete(String questName) {
        return this.completedQuests.stream().filter(e -> e.quest().getName().equals(questName)).findFirst();
    }

    @Override
    public Optional<CompletedQuest> findComplete(Quest quest) {
        return this.findComplete(quest.getName());
    }

    @Override
    public boolean isExtend() {
        return this.isExtend;
    }

    @Override
    public void setExtend(boolean extend) {
        this.isExtend = extend;
    }

    @Override
    public String getCurrentGroup() {
        return this.currentGroup;
    }

    @Override
    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }
}
