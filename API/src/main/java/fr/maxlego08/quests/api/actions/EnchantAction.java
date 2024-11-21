package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantAction extends ActionInfo<EnchantItemEvent> {

    public EnchantAction(QuestType questType, EnchantItemEvent value) {
        super(questType, value);
    }
}
