package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.inventory.BrewEvent;

public class BrewAction extends ActionInfo<BrewEvent> {
    public BrewAction(QuestType questType, BrewEvent value) {
        super(questType, value);
    }
}
