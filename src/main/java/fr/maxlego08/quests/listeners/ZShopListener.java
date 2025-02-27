package fr.maxlego08.quests.listeners;

import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.zshop.api.event.ShopAction;
import fr.maxlego08.zshop.api.event.events.ZShopSellAllEvent;
import fr.maxlego08.zshop.api.event.events.ZShopSellEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class ZShopListener implements Listener {

    private final QuestManager manager;

    public ZShopListener(QuestManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSell(ZShopSellEvent event) {
        var player = event.getPlayer();
        var itemType = event.getItemButton().getItemStack().build(player).getType();
        this.manager.handleStaticQuests(player.getUniqueId(), QuestType.SELL, event.getAmount(), itemType);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSell(ZShopSellAllEvent event) {
        var player = event.getPlayer();
        Map<Material, Integer> map = new HashMap<>();
        for (ShopAction shopAction : event.getShopActions()) {
            var itemStack = shopAction.getItemStack();
            map.put(itemStack.getType(), map.getOrDefault(itemStack.getType(), 0) + itemStack.getAmount());
        }
        map.forEach((itemType, amount) -> this.manager.handleStaticQuests(player.getUniqueId(), QuestType.SELL, amount, itemType));
    }

}
