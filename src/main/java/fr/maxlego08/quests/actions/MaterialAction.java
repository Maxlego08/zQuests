package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;

public class MaterialAction implements QuestAction {

    private final Material target;
    private final QuestType questType;

    public MaterialAction(Material target, QuestType questType) {
        this.target = target;
        this.questType = questType;
    }

    @Override
    public QuestType getQuestType() {
        return questType;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof Material material && material == this.target;
    }
}
