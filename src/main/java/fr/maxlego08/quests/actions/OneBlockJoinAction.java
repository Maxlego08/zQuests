package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;

/** Matches the role of the player on his OneBlock island: "chef" (owner), "membre" or "*" for both. */
public class OneBlockJoinAction implements QuestAction {
    private final String role;

    public OneBlockJoinAction(String role) {
        this.role = role;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof String value && (role.equals("*") || role.equals(value));
    }

    @Override
    public QuestType getQuestType() { return QuestType.OB_JOIN; }
}
