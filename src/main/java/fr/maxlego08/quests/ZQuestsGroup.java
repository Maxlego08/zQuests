package fr.maxlego08.quests;

import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestsGroup;
import fr.maxlego08.quests.api.UserQuest;

import java.util.List;

public class ZQuestsGroup implements QuestsGroup {

    private final String name;
    private final String displayName;
    private final List<Quest> quests;

    public ZQuestsGroup(String name, String displayName, List<Quest> quests) {
        this.name = name;
        this.displayName = displayName;
        this.quests = quests;
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
}
