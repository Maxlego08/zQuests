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
import org.bukkit.block.Crafter;
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
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class QuestListener implements Listener {

    private static final Set<InventoryType> TRACKABLE_INVENTORIES = EnumSet.of(InventoryType.BREWING, InventoryType.FURNACE, InventoryType.BLAST_FURNACE, InventoryType.SMOKER, InventoryType.CRAFTER);
    private static final Set<Material> TRACKABLE_BLOCKS = EnumSet.of(Material.BREWING_STAND, Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER, Material.CRAFTER);

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

        this.plugin.debug("Start Break Block " + player.getName() + " : " + block.getType() + " - world: " + block.getWorld().getName() + " x: " + block.getX() + " y: " + block.getY() + " z: " + block.getZ());

        if (isNPC(player)) return;

        Material material = block.getType();

        this.plugin.debug("Break Block: PLAYER IS NOT AN NPC " + block.getBlockData() + " -> " + (block.getBlockData() instanceof Ageable));

        if (!(block.getBlockData() instanceof Ageable)) {

            this.plugin.debug("Break Block: tracked: " + this.plugin.getBlockHook().isTracked(block));
            if (this.plugin.getBlockHook().isTracked(block)) return;

            this.manager.handleQuests(player.getUniqueId(), QuestType.BLOCK_BREAK, 1, event.getBlock().getType());

        } else if (block.getBlockData() instanceof Ageable ageable) {

            this.plugin.debug("Break Block: Ageable: " + ageable.getAge() + " / " + ageable.getMaximumAge() + " - is special block: " + material + " = " + (material == Material.SUGAR_CANE || material == Material.KELP || material == Material.BAMBOO));
            if ((material == Material.SUGAR_CANE || material == Material.KELP || material == Material.BAMBOO) || ageable.getAge() == ageable.getMaximumAge()) {
                this.manager.handleQuests(player.getUniqueId(), QuestType.FARMING, 1, event.getBlock().getType());
            }
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
        if (!(event.getWhoClicked() instanceof Player player) || isNPC(player)) return;

        Inventory inventory = event.getInventory();
        if (!isTrackableInventory(inventory)) return;

        if (inventory.getHolder() instanceof Container container) {
            updateContainerOwner(container, player);
        }
    }

    private boolean isTrackableInventory(Inventory inventory) {
        return TRACKABLE_INVENTORIES.contains(inventory.getType());
    }

    private void updateContainerOwner(Container container, Player player) {
        Block block = container.getBlock();
        if (!isTrackableBlock(block)) return;

        if (block.getState() instanceof Container containerState) {
            PersistentDataContainer pdc = containerState.getPersistentDataContainer();
            pdc.set(playerKey, PersistentDataType.STRING, player.getUniqueId().toString());
            containerState.update();
        }
    }

    private boolean isTrackableBlock(Block block) {
        return TRACKABLE_BLOCKS.contains(block.getType());
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
        if (!isValidCraftEvent(event)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack result = event.getCurrentItem();

        int craftAmount = event.isShiftClick() && event.getClick() != ClickType.CONTROL_DROP ? calculateMaxCraftAmount(event) : event.getCursor().getType() != Material.AIR ? 0 : result.getAmount();

        if (craftAmount > 0 && !isNPC(player)) {
            this.manager.handleQuests(player.getUniqueId(), QuestType.CRAFT, craftAmount, result);
        }
    }

    private boolean isValidCraftEvent(CraftItemEvent event) {
        return event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getAction() != InventoryAction.NOTHING && !isInvalidDrop(event) && event.getWhoClicked() instanceof Player player && player.getInventory().getItemInOffHand().getAmount() == 0;
    }

    private boolean isInvalidDrop(CraftItemEvent event) {
        return (event.getAction() == InventoryAction.DROP_ONE_SLOT && event.getClick() == ClickType.DROP && event.getCursor().getType() != Material.AIR) || (event.getAction() == InventoryAction.DROP_ALL_SLOT && event.getClick() == ClickType.CONTROL_DROP && event.getCursor().getType() != Material.AIR) || (event.getAction() == InventoryAction.UNKNOWN && event.getClick() == ClickType.UNKNOWN);
    }

    private int calculateMaxCraftAmount(CraftItemEvent event) {
        int eventAmount = event.getCurrentItem().getAmount();
        int maxAmount = Arrays.stream(event.getInventory().getMatrix()).filter(item -> item != null && item.getType() != Material.AIR).mapToInt(ItemStack::getAmount).min().orElse(event.getInventory().getMaxStackSize());
        return Math.min(eventAmount * maxAmount, getAvailableSpace((Player) event.getWhoClicked(), event.getCurrentItem()));
    }

    private int getAvailableSpace(Player player, ItemStack newItemStack) {
        PlayerInventory inventory = player.getInventory();
        int availableSpace = inventory.all(newItemStack.getType()).values().stream().filter(newItemStack::isSimilar).mapToInt(item -> newItemStack.getMaxStackSize() - item.getAmount()).sum();
        availableSpace += Arrays.stream(inventory.getStorageContents()).filter(Objects::isNull).mapToInt(item -> newItemStack.getMaxStackSize()).sum();
        return availableSpace;
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSmith(SmithItemEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (isNPC(player)) return;
            var result = event.getInventory().getResult();
            if (result == null) return;
            this.manager.handleQuests(player.getUniqueId(), QuestType.SMITHING, 1, result.getType());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCrafter(CrafterCraftEvent event) {
        var result = event.getResult();
        var block = event.getBlock();

        if (block.getState() instanceof Crafter crafter) {
            PersistentDataContainer container = crafter.getPersistentDataContainer();

            if (container.has(playerKey, PersistentDataType.STRING)) {
                var uuidString = container.get(playerKey, PersistentDataType.STRING);
                if (uuidString == null) return;

                var playerUUID = UUID.fromString(uuidString);
                Player player = Bukkit.getPlayer(playerUUID);

                if (isNPC(player)) return;

                if (player != null) {
                    this.manager.handleQuests(player.getUniqueId(), QuestType.CRAFT, result.getAmount(), result);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        var player = event.getPlayer();
        if (isNPC(player)) return;

        this.manager.handleQuests(player.getUniqueId(), QuestType.COMMAND, 1, event.getMessage().substring(1));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerMoveEvent event) {

        if (!event.hasExplicitlyChangedBlock()) return;

        var player = event.getPlayer();
        if (isNPC(player)) return;

        this.manager.handleQuests(player.getUniqueId(), QuestType.CUBOID, 1, event.getTo());
    }

}
