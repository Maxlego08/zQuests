package fr.maxlego08.quests;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.hologram.QuestHologram;
import fr.maxlego08.quests.save.Config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ZUserQuest implements UserQuest {

    private final UUID uniqueId;
    private final List<ActiveQuest> activeQuests;
    private final List<CompletedQuest> completedQuests;
    private final List<QuestHologram> questHolograms = new ArrayList<>();
    private String currentGroup;
    private boolean isExtend;
    private int favoriteAmount;

    public ZUserQuest(UUID uniqueId) {
        this(uniqueId, new ArrayList<>(), new ArrayList<>(), Config.placeholderFavorite.limit());
    }

    public ZUserQuest(UUID uniqueId, List<ActiveQuest> activeQuests, List<CompletedQuest> completedQuests, int favoriteAmount) {
        this.uniqueId = uniqueId;
        this.activeQuests = activeQuests;
        this.completedQuests = completedQuests;
        this.favoriteAmount = favoriteAmount;
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
    public List<QuestHologram> getHolograms() {
        return this.questHolograms;
    }

    @Override
    public void addHologram(QuestHologram questHologram) {
        this.questHolograms.add(questHologram);
    }

    @Override
    public void removeHologram(QuestHologram questHologram) {
        this.questHolograms.remove(questHologram);
    }

    @Override
    public String getCurrentGroup() {
        return this.currentGroup;
    }

    @Override
    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public Optional<QuestHologram> getHologram(Quest quest) {
        String name = quest.getHologramName(this.uniqueId);
        return this.questHolograms.stream().filter(questHologram -> questHologram.match(name)).findFirst();
    }

    @Override
    public boolean isFavorite(String questId) {
        return this.activeQuests.stream().anyMatch(e -> e.getQuest().getName().equals(questId) && e.isFavorite());
    }

    @Override
    public int getFavoriteAmount() {
        return this.favoriteAmount;
    }

    @Override
    public void setFavoriteAmount(int favoriteAmount) {
        this.favoriteAmount = favoriteAmount;
    }
}
