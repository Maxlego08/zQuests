package fr.maxlego08.quests;

import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestsGroup;
import fr.maxlego08.quests.api.UserQuest;

import java.util.List;

public class ZQuestsGroup implements QuestsGroup {

    private final String name;
    private final String displayName;
    private final List<String> questNames;
    private final List<QuestsGroup> subGroups;
    private final int defaultCustomModelId;
    private final boolean isProgression;
    private List<Quest> quests;

    public ZQuestsGroup(String name, String displayName, List<String> quests, List<QuestsGroup> subGroups, int defaultCustomModelId, boolean isProgression) {
        this.name = name;
        this.displayName = displayName;
        this.questNames = quests;
        this.subGroups = subGroups;
        this.defaultCustomModelId = defaultCustomModelId;
        this.isProgression = isProgression;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<Quest> getQuests() {
        return quests;
    }

    @Override
    public void setQuests(List<Quest> quests) {
        this.quests = quests;
    }

    @Override
    public List<String> getQuestNames() {
        return this.questNames;
    }

    @Override
    public boolean needFavorite(Quest quest, UserQuest userQuest) {

        for (Quest groupQuest : quests) {

            var optional = userQuest.findActive(groupQuest);
            if (optional.isPresent()) {
                var activeQuest = optional.get();
                if (activeQuest.isFavorite()) {
                    return true;
                }
            }

            if (quest == groupQuest) {
                return false;
            }
        }

        return false;
    }

    @Override
    public int getDefaultCustomModelId() {
        return this.defaultCustomModelId;
    }

    @Override
    public boolean isProgression() {
        return isProgression;
    }

    @Override
    public boolean contains(String groupName) {
        return this.name.equalsIgnoreCase(groupName) || this.subGroups.stream().anyMatch(e -> e.contains(groupName));
    }

    @Override
    public List<QuestsGroup> getSubGroups() {
        return subGroups;
    }
}
