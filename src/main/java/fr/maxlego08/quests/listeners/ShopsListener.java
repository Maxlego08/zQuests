package fr.maxlego08.quests.listeners;

import fr.kazotaruu.shops.api.event.AuctionPurchasedEvent;
import fr.kazotaruu.shops.api.event.OrderCompleteEvent;
import fr.kazotaruu.shops.api.event.ShopTransactionCompleteEvent;
import fr.kazotaruu.shops.api.event.ShopTransactionEvent;
import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ShopsListener implements Listener {
    private final QuestManager manager;

    public ShopsListener(QuestManager manager) { this.manager = manager; }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onShopTransaction(ShopTransactionCompleteEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        var type = event.getType() == ShopTransactionEvent.Type.BUY ? QuestType.BUY_SHOP : QuestType.SELL_SHOP;
        manager.handleQuests(event.getPlayer().getUniqueId(), type, event.getAmount(), event.getItem().key());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAuctionBuy(AuctionPurchasedEvent event) {
        if (event.getPlayer().hasMetadata("NPC")) return;
        var item = event.getListing().item();
        manager.handleQuests(event.getPlayer().getUniqueId(), QuestType.AH_BUY, item.getAmount(), item.getType().name());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOrderComplete(OrderCompleteEvent event) {
        var order = event.getOrder();
        if (order.worker() == null) return;
        manager.handleQuests(order.worker(), QuestType.ORDER_COMPLETE, 1, order.itemKey());
    }
}
