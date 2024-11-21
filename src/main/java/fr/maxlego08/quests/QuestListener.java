package fr.maxlego08.quests;

import fr.maxlego08.quests.api.QuestManager;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Container;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class QuestListener implements Listener {

    private final QuestsPlugin plugin;
    private final QuestManager manager;
    private final NamespacedKey playerKey;

    public QuestListener(QuestsPlugin plugin, QuestManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        this.playerKey = new NamespacedKey(plugin, "player-uuid");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.manager.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.manager.handleQuit(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        Player player = event.getPlayer();
        Material material = block.getType();

        if (!(block.getBlockData() instanceof Ageable)) {

            if (this.plugin.getBlockHook().isTracked(block)) return;

            this.manager.handleQuests(player.getUniqueId(), QuestType.BLOCK_BREAK, 1, event.getBlock().getType());

        } else if (block.getBlockData() instanceof Ageable ageable && ((material == Material.SUGAR_CANE || material == Material.KELP || material == Material.BAMBOO) || ageable.getAge() == ageable.getMaximumAge())) {

            this.manager.handleQuests(player.getUniqueId(), QuestType.FARMING, 1, event.getBlock().getType());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Material material = block.getType();

        this.manager.handleQuests(player.getUniqueId(), QuestType.BLOCK_PLACE, 1, material);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {

        Player player = event.getPlayer();
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item item) {

            ItemStack itemStack = item.getItemStack();
            this.manager.handleQuests(player.getUniqueId(), QuestType.FISHING, 1, itemStack);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnimalTame(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();
        if (entity.getKiller() != null) {

            var killer = entity.getKiller();
            var amount = this.plugin.getStackerHook().getEntityCount(entity);

            this.manager.handleQuests(killer.getUniqueId(), QuestType.ENTITY_KILL, amount, entity.getType());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnimalTame(EntityTameEvent event) {

        LivingEntity animal = event.getEntity();

        if (animal.isDead()) return;

        if (event.getOwner() instanceof Player player && player.isOnline()) {
            this.manager.handleQuests(player.getUniqueId(), QuestType.TAME, 1, animal.getType());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory instanceof EnchantingInventory enchantingInventory) {
            ItemStack itemStack = enchantingInventory.getItem();
            if (itemStack == null) return;

            Player player = event.getEnchanter();
            this.manager.handleQuests(player.getUniqueId(), QuestType.ENCHANT, 1, event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        var inventory = event.getInventory();
        if (event.getWhoClicked() instanceof Player player) {
            if ((inventory.getType() == InventoryType.BREWING || inventory.getType() == InventoryType.FURNACE || inventory.getType() == InventoryType.BLAST_FURNACE || inventory.getType() == InventoryType.SMOKER) && inventory.getHolder() instanceof Container container) {

                var block = container.getBlock();
                if (block.getType() == Material.BREWING_STAND || block.getType() == Material.FURNACE || block.getType() == Material.BLAST_FURNACE || block.getType() == Material.SMOKER) {
                    var containerState = (Container) block.getState();

                    var playerUUID = player.getUniqueId();

                    var persistentDataContainer = containerState.getPersistentDataContainer();
                    persistentDataContainer.set(playerKey, PersistentDataType.STRING, playerUUID.toString());

                    containerState.update();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrew(BrewEvent event) {
        var brewerInventory = event.getContents();
        var block = brewerInventory.getHolder().getBlock();

        if (block.getType() == Material.BREWING_STAND) {
            var brewingStand = (BrewingStand) block.getState();
            var container = brewingStand.getPersistentDataContainer();

            if (container.has(playerKey, PersistentDataType.STRING)) {
                var uuidString = container.get(playerKey, PersistentDataType.STRING);
                if (uuidString == null) return;

                var playerUUID = UUID.fromString(uuidString);
                Player player = Bukkit.getPlayer(playerUUID);

                if (player != null) {
                    this.manager.handleQuests(player.getUniqueId(), QuestType.BREW, 1, event);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.FURNACE || block.getType() == Material.BLAST_FURNACE || block.getType() == Material.SMOKER) {
            Furnace furnace = (Furnace) block.getState();
            PersistentDataContainer container = furnace.getPersistentDataContainer();

            if (container.has(playerKey, PersistentDataType.STRING)) {
                var uuidString = container.get(playerKey, PersistentDataType.STRING);
                if (uuidString == null) return;

                var playerUUID = UUID.fromString(uuidString);
                Player player = Bukkit.getPlayer(playerUUID);

                if (player != null) {
                    this.manager.handleQuests(player.getUniqueId(), QuestType.SMELT, 1, event);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        var result = event.getRecipe().getResult();
        this.manager.handleQuests(event.getWhoClicked().getUniqueId(), QuestType.CRAFT, result.getAmount(), result.getType());
    }

}
