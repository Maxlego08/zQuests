package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;
import org.bukkit.Tag;

public class TagAction implements QuestAction {

    private final Tag<Material> target;
    private final QuestType questType;

    public TagAction(Tag<Material> target, QuestType questType) {
        this.target = target;
        this.questType = questType;
    }

    @Override
    public QuestType getQuestType() {
        return questType;
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof Material material && this.target.isTagged(material);
    }
}
