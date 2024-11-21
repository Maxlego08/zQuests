package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Material;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class BrewAction implements QuestAction {

    private final PotionType target;
    private final QuestType questType;
    private final Material potionMaterial;
    private final Material ingredient;

    public BrewAction(PotionType target, QuestType questType, Material potionMaterial, Material ingredient) {
        this.target = target;
        this.questType = questType;
        this.potionMaterial = potionMaterial;
        this.ingredient = ingredient;
    }

    @Override
    public QuestType getQuestType() {
        return questType;
    }

    @Override
    public boolean isAction(Object target) {

        if (target instanceof BrewEvent event) {

            var result = event.getResults();
            var contents = event.getContents();
            var ingredient = contents.getIngredient();

            if (!result.isEmpty()) {
                ItemStack itemStack = result.getFirst();

                if (this.potionMaterial == itemStack.getType()) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta instanceof PotionMeta potionMeta) {

                        if (potionMeta.getBasePotionType() == this.target) {
                            return true;
                        }
                    }
                }
            }

            return ingredient != null && ingredient.getType() == this.ingredient;
        }

        return false;
    }
}
