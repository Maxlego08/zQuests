package fr.maxlego08.quests.listeners;

import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.shop.api.event.ShopAction;
import fr.maxlego08.shop.api.event.events.ZShopBuyEvent;
import fr.maxlego08.shop.api.event.events.ZShopSellAllEvent;
import fr.maxlego08.shop.api.event.events.ZShopSellEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class ZShopListener implements Listener {

    private final QuestsPlugin plugin;
    private final QuestManager manager;

    public ZShopListener(QuestsPlugin plugin, QuestManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSell(ZShopBuyEvent event) {
        var player = event.getPlayer();
        var itemType = event.getItemButton().getItemStack().build(player).getType();
        var r = this.manager.handleQuests(player.getUniqueId(), QuestType.PURCHASE, event.getAmount(), itemType);

        this.plugin.debug(QuestType.PURCHASE, "zShop purchase " + itemType + " -> " + player.getName() + " -> " + r);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSell(ZShopSellEvent event) {
        var player = event.getPlayer();
        var itemType = event.getItemButton().getItemStack().build(player).getType();
        var r = this.manager.handleQuests(player.getUniqueId(), QuestType.SELL, event.getAmount(), itemType);

        this.plugin.debug(QuestType.SELL, "zShop sell " + itemType + " -> " + player.getName() + " -> " + r);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSell(ZShopSellAllEvent event) {
        var player = event.getPlayer();
        Map<Material, Integer> map = new HashMap<>();
        for (ShopAction shopAction : event.getShopActions()) {
            var itemStack = shopAction.getItemStack();
            map.put(itemStack.getType(), map.getOrDefault(itemStack.getType(), 0) + itemStack.getAmount());
        }
        map.forEach((itemType, amount) -> this.manager.handleQuests(player.getUniqueId(), QuestType.SELL, amount, itemType));

        this.plugin.debug(QuestType.SELL, "zShop sell all -> " + player.getName());
    }

}
