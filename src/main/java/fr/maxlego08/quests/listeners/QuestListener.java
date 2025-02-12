package fr.maxlego08.quests.listeners;

import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import fr.maxlego08.quests.QuestsPlugin;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;
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

    private boolean isNPC(Player player) {
        return player != null && player.hasMetadata("NPC");
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

        if (isNPC(player)) return;

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

        if (isNPC(player)) return;

        Material material = block.getType();

        this.manager.handleQuests(player.getUniqueId(), QuestType.BLOCK_PLACE, 1, material);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {

        Player player = event.getPlayer();

        if (isNPC(player)) return;

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item item) {

            ItemStack itemStack = item.getItemStack();
            this.manager.handleQuests(player.getUniqueId(), QuestType.FISHING, 1, itemStack.getType());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnimalTame(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();
        if (entity.getKiller() != null) {

            var player = entity.getKiller();
            if (isNPC(player)) return;

            var amount = this.plugin.getStackerHook().getEntityCount(entity);

            this.manager.handleQuests(player.getUniqueId(), QuestType.ENTITY_KILL, amount, entity.getType());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnimalTame(EntityTameEvent event) {

        LivingEntity animal = event.getEntity();

        if (animal.isDead()) return;

        if (event.getOwner() instanceof Player player && player.isOnline()) {

            if (isNPC(player)) return;

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
            if (isNPC(player)) return;

            this.manager.handleQuests(player.getUniqueId(), QuestType.ENCHANT, 1, event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        var inventory = event.getInventory();
        if (event.getWhoClicked() instanceof Player player) {

            if (isNPC(player)) return;

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

        ItemStack[] contents = event.getContents().getContents();
        List<ItemStack> results = event.getResults();

        int eventAmount = 0;
        for (int i = 0; i < results.size(); i++) {
            if (contents[i] != null && !contents[i].isSimilar(results.get(i))) {
                eventAmount++;
            }
        }

        if (eventAmount > 0) {
            var block = brewerInventory.getHolder().getBlock();
            if (block.getType() == Material.BREWING_STAND) {
                var brewingStand = (BrewingStand) block.getState();
                var container = brewingStand.getPersistentDataContainer();

                if (container.has(playerKey, PersistentDataType.STRING)) {
                    var uuidString = container.get(playerKey, PersistentDataType.STRING);
                    if (uuidString != null) {
                        var playerUUID = UUID.fromString(uuidString);
                        Player player = Bukkit.getPlayer(playerUUID);

                        if (isNPC(player)) return;

                        if (player != null) {
                            this.manager.handleQuests(player.getUniqueId(), QuestType.BREW, eventAmount, event);
                        }
                    }
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

                if (isNPC(player)) return;

                if (player != null) {
                    this.manager.handleQuests(player.getUniqueId(), QuestType.SMELT, 1, event.getResult().getType());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();

        int craftAmount;

        if (event.isShiftClick()) {

            craftAmount = calculateMaxCraftAmount(event);
        } else {

            craftAmount = event.getCursor() != null && event.getCursor().getType() != Material.AIR ? 0 : result.getAmount();
        }

        if (craftAmount == 0) return;

        if (event.getWhoClicked() instanceof Player player) {

            if (isNPC(player)) return;

            this.manager.handleQuests(player.getUniqueId(), QuestType.CRAFT, craftAmount, result.getType());
        }
    }

    private int calculateMaxCraftAmount(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        int maxStackSize = result.getMaxStackSize();

        InventoryView view = event.getView();
        CraftingInventory inventory = (CraftingInventory) view.getTopInventory();

        int maxPossibleCrafts = Integer.MAX_VALUE;
        for (ItemStack item : inventory.getMatrix()) {
            if (item != null && item.getType() != Material.AIR) {
                maxPossibleCrafts = Math.min(maxPossibleCrafts, item.getAmount());
            }
        }

        Inventory playerInventory = event.getWhoClicked().getInventory();
        int availableSpace = 0;

        for (ItemStack item : playerInventory.getStorageContents()) {
            if (item == null || item.getType() == Material.AIR) {
                availableSpace += maxStackSize;
            } else if (item.isSimilar(result)) {
                availableSpace += maxStackSize - item.getAmount();
            }
        }

        return Math.min(maxPossibleCrafts * result.getAmount(), availableSpace / result.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onThrownEggHatch(ThrownEggHatchEvent event) {
        int numHatches = event.getNumHatches();
        if (numHatches == 0) {
            return;
        }

        ProjectileSource shooter = event.getEgg().getShooter();
        if (!(shooter instanceof Player player)) {
            return;
        }

        if (isNPC(player)) return;

        this.manager.handleQuests(player.getUniqueId(), QuestType.HATCHING, numHatches, event.getEgg());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExpEarn(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();

        if (isNPC(player)) return;

        this.manager.handleQuests(player.getUniqueId(), QuestType.EXPERIENCE_GAIN, event.getAmount(), event.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemBreak(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();

        if (isNPC(player)) return;

        this.manager.handleQuests(player.getUniqueId(), QuestType.ITEM_BREAK, 1, event.getBrokenItem().getType());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemMed(PlayerItemMendEvent event) {
        Player player = event.getPlayer();

        if (isNPC(player)) return;

        this.manager.handleQuests(player.getUniqueId(), QuestType.ITEM_MENDING, event.getRepairAmount(), event.getItem().getType());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player player)) {
            return;
        }

        if (isNPC(player)) return;


        this.manager.handleQuests(player.getUniqueId(), QuestType.ENTITY_DAMAGE, (int) event.getDamage(), event.getDamage());
    }

}
