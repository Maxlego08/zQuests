package fr.maxlego08.quests.api;

public interface QuestAction {

    boolean isAction(Object target);

    QuestType getQuestType();

}
