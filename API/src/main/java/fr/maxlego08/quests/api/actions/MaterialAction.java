package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;

public class MaterialAction extends ActionInfo<Material> {
    public MaterialAction(QuestType questType, Material value) {
        super(questType, value);
    }
}
