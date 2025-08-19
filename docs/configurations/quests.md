# 🐎 Quests

## Configuration

This section explains each element of the quest configuration, providing a detailed understanding of how to set up quests in your Minecraft server.

```yml
quests:
  - type: BLOCK_BREAK # The type of the quest
    actions:
      - material: STONE
      - material: COBBLESTONE
    name: "block-break-1" # The name must be unique and must never change
    display-name: "Stone Breaker" # The name of the quest to be displayed in-game
    description: "Break 500 stone blocks" # The description of the quest
    thumbnail: STONE # The material to be displayed in-game
    goal: 500 # The goal to be achieved
    auto-accept: true # If the quest should start automatically
    use-global-rewards: true # Allows you to use global rewards
    favorite: false # Allows to bookmark the quest when it starts
    can-change-favorite: true # Allows you to define whether the player can activate or not the favorites on the quest
    unique: false # Allows you to define if a quest is unique
    custom-model-id: 0 # Allows to define the custom model id for the placeholder %quest-model-id% for the ZQUEST_HISTORY button
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &7500 stone&f blocks!"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"
```

***

### `type`

Specifies the type of the quest. This can be `BLOCK_BREAK`, `BLOCK_PLACE`, `ENTITY_KILL`, etc. It determines the main objective of the quest.

**Example:**

```yml
type: BLOCK_BREAK
```

***

### `actions`

Defines the specific actions required to complete the quest. It can include one or more materials or tags that the player must interact with. Certain types of quests do not require action.

**Example:**

```yml
actions:
  - material: STONE
  - material: COBBLESTONE
```

***

### `name`

A unique identifier for the quest. It must be unique across all quests and should never change once set.

**Example:**

```yml
name: "block-break-1"
```

{% hint style="warning" %}
Be careful, the name of the quest must be unique!
{% endhint %}

***

### `display-name`

The name of the quest as displayed in-game. This name is visible to the players and can be descriptive.

The default value will be the name of the quest.

**Example:**

```yml
display-name: "Stone Breaker"
```

***

### `description`

Provides a description of the quest that players can read to understand what is required to complete it.

Default value will be `no description`

**Example:**

```yml
description: "Break 500 stone blocks"
```

***

### `placeholder-description`

Allows you to display the description using placeholders. This can be used with the placeholder configuration system.

**Example:**

```yaml
placeholder-description: "Break %quest-remaining% stone blocks"
```

***

### `thumbnail`

Defines the material that will be displayed as the thumbnail icon for the quest in the quest menu.

No default value, you are not required to specify the thumbnail.

**Example:**

```yml
thumbnail: STONE
```

***

### `goal`

The number of times the action must be performed to complete the quest. This could be the number of blocks broken, items crafted, etc.

The goal is <mark style="color:red;">mandatory</mark>, by default the value will be **1**.

**Example:**

```yml
goal: 500
```

***

### `auto-accept`

Determines whether the quest should be automatically accepted when available to the player. Set to `true` or `false`. By default a quest will always be false

**Example:**

```yml
auto-accept: true
```

***

### `use-global-reward`

Sets whether global rewards will be given when the quest is completed.

**Example:**

```yaml
use-global-reward: true
```

***

### `favorite`

Allows you to define if a quest will be in favorites when it starts. The player can manage favorite quests with the `ZQUESTS_HISTORY` button.

**Example:**

```yaml
favorite: false
```

***

### `can-change-favorite`

Sets whether the player can change a quest to favorite or not.

**Example:**

```yaml
can-change-favorite: true
```

***

### `unique`

Allows you to define if when advancing this quest, it prevents the progression of other quests. You can use this for quests with citizens for example.

**Example:**

```yaml
unique: false
```

***

### `hidden`

Allows you to define if a quest is invisible in the `ZQUESTS_HISTORY` button.

**Example:**

```yaml
hidden: false
```

***

### `custom-model-id`

Allows you to define the custom model id of the quest that will be used in various placeholders.

**Example:**

```yaml
custom-model-id: 0
```

***

### `rewards`

Defines the rewards given to the player upon completing the quest. Multiple types of rewards can be specified.&#x20;

{% hint style="info" %}
You can use all [actions](https://docs.zmenu.dev/configurations/buttons/actions) of zMenu
{% endhint %}

**Example:**

```yml
rewards:
  - type: message
    messages:
      - "&fYou have broken &7500 stone&f blocks!"
  - type: console_command
    commands:
      - "give %player% diamond 5"
```

### `start-actions`

Allows to define the actions to be performed when the quest will start

{% hint style="info" %}
You can use all [actions](https://docs.zmenu.dev/configurations/buttons/actions) of zMenu
{% endhint %}

**Example:**

```yaml
start-actions:
  - type: message
    messages:
      - "&fGood luck !"
```

***

## Quests type

### `BLOCK_BREAK`

This quest requires the player to break a certain number of specific blocks. It can be used to encourage players to mine or destroy blocks in a specific area.

In actions you can use a [material](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) or [tag](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Tag.html).

**Example usage:**

```yml
quests:
  - type: BLOCK_BREAK # The type of the quest
    actions:
      - material: STONE # https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
      - material: COBBLESTONE # https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
      - tag: LOGS # https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Tag.html
    name: "block-break-1" # The name must be unique and must never change
    display-name: "Example 1" # The name of the quest to be displayed in-game
    description: "Break 500 stone blocks or logs" # The description of the quest
    thumbnail: STONE # The material to be displayed in-game
    goal: 500 # The goal to be achieved
```

***

### `BLOCK_PLACE`

This quest requires the player to place a certain number of specific blocks. It can be used to build specific structures or buildings.

**Example usage:**

```yml
quests:
  - type: BLOCK_PLACE # The type of the quest
    actions:
      - material: STONE # https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
      - material: COBBLESTONE # https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
      - tag: LOGS # https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Tag.html
    name: "block-place-1" # The name must be unique and must never change
    display-name: "Example 1" # The name of the quest to be displayed in-game
    description: "Place 500 stone blocks or logs" # The description of the quest
    thumbnail: STONE # The material to be displayed in-game
    goal: 500 # The goal to be achieved
```

***

### `ENTITY_KILL`

This quest involves killing a given number of [entities ](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html)(mobs). It is commonly used to encourage combat against monsters.

**Example usage:**

```yml
quests:
  - type: ENTITY_KILL # The type of the quest
    actions: # List of entities to be considered
      - entity: SKELETON # https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html
    name: "entity-skeleton-1" # The name must be unique and must never change
    display-name: "Skeleton Killer" # The name of the quest to be displayed in-game
    description: "Kill 10 Skeleton" # The description of the quest
    thumbnail: BONE # The material to be displayed in-game
    goal: 10 # The goal to be achieved
```

***

### `FARMING`

The Farming quest requires players to harvest crops. It is ideal for servers with a focus on agriculture and resource management.

**Example usage:**

```yml
quests:
  - type: FARMING # The type of the quest
    actions:
      - material: WHEAT
    name: "farming-wheat-1" # The name must be unique and must never change
    display-name: "Wheat Breaker" # The name of the quest to be displayed in-game
    description: "Break 500 wheat blocks" # The description of the quest
    thumbnail: WHEAT # The material to be displayed in-game
    goal: 10 # The goal to be achieved
```

***

### `FISHING`

This quest involves fishing a certain number of fish. It can encourage players to spend time near water and explore aquatic environments.

**Example usage:**

```yml
quests:
  - type: FISHING # The type of the quest
    actions: # Actions to be performed in the quest
      - material: COD # Type of fish to be caught
    name: "fishing-cod-1" # The name must be unique and must never change
    display-name: "Fishing Cod" # The name of the quest to be displayed in-game
    description: "Catch 8 cod" # The description of the quest
    thumbnail: COD # The material to be displayed in-game
    goal: 8 # The goal to be achieved
```

***

### `TAME`

This quest involves taming an animal. It is perfect for players who enjoy interacting with the game's wildlife.

**Example usage:**

```yml
quests:
  - type: TAME # The type of the quest
    actions: # List of entities to be considered
      - entity: HORSE
    name: "tame-horse-1" # The name must be unique and must never change
    display-name: "Tame horse" # The name of the quest to be displayed in-game
    description: "Tame 10 horses" # The description of the quest
    thumbnail: BONE # The material to be displayed in-game
    goal: 10 # The goal to be achieved
```

***

### `ENCHANT`

The player must enchant an item with a specific enchantment. This encourages players to use enchantment tables and improve their gear.

**Example usage:**

```yml
quests:
  - type: ENCHANT
    actions:
      - enchantment: SHARPNESS
    name: "enchant-sharpness-1"
    display-name: "Enchant Sharpness"
    description: "Enchant 8 items with sharpness"
    thumbnail: ENCHANTED_BOOK
    goal: 8
```

For enchantment quests, you need to specify the following details:

* `enchantment` : Indicate the specific enchantment you want to apply (e.g., SHARPNESS, FORTUNE).
* `material` : Specify the material on which the enchantment can be applied, such as books, tools, or weapons.
* `minimum-level` : Determine the minimum level required to perform the enchantment.
* `minimum-cost` : The minimum experience points cost required to proceed with the enchantment.

These elements are used in the configuration file to precisely define the challenge and the resources needed to complete the enchantment quest.

***

### `BREW`

This quest involves brewing potions. It is ideal for players who enjoy experimenting with potion recipes.

**Example usage:**

```yml
quests:
  - type: BREW # The type of the quest
    actions: # Actions to be performed in the quest
      - potion-type: SWIFTNESS # Type of potion to be brewed
    name: "brew-swiftness-3" # The name must be unique and must never change
    display-name: "Brew 3 swiftness" # The name of the quest to be displayed in-game
    description: "Make 3 swiftness potions" # The description of the quest
    thumbnail: POTION # The material to be displayed in-game
    goal: 3 # The goal to be achieved
```

For BREW quests, you must also include the following specific details regarding the potion:

* `potion-type`: The[ type of potion](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionType.html) to be brewed (e.g., SWIFTNESS).
* `potion-material`: Specifies the material type, either `POTION` or `SPLASH_POTION`.
* `ingredient`: The material used as an ingredient in the potion recipe.

***

### `SMELT`

This quest requires the player to smelt items in a furnace. It is often used for tasks involving resource refinement.

**Example usage:**

```yml
quests:
  - type: SMELT # The type of the quest
    actions: # Actions to be performed in the quest
      - material: IRON_INGOT # Type of material to be smelted
    name: "smelt-iron-1" # The name must be unique and must never change
    display-name: "Smelt Iron" # The name of the quest to be displayed in-game
    description: "Smelt 8 iron ingots" # The description of the quest
    thumbnail: IRON_INGOT # The material to be displayed in-game
    goal: 8 # The goal to be achieved
```

***

### `CRAFT`

The player must craft a specific item using a crafting table. This encourages players to gather resources and transform them.

**Example usage:**

```yml
quests:
  - type: CRAFT
    actions:
      - material: CHEST
    name: "craft-chest-1"
    display-name: "Craft 8 Chests"
    description: "Craft 8 chests"
    thumbnail: CHEST
    goal: 8
```

***

### `VOTE`

This quest rewards players for voting for the server on ranking sites. This helps promote the server.

For now, you must use the `/quest add-progress <player> <quest> <amount>` command to increase this quest

**Example usage:**

```yml
quests:
  - type: VOTE
    actions: []
    name: "vote-1"
    display-name: "Vote 4 times"
    description: "Vote 4 times"
    goal: 4
```

***

### `SELL`

The player must sell items via a trading system. This encourages players to participate in the server economy.

**Example usage:**

```yml
quests:
  - type: SELL
    actions:
      - material: IRON_INGOT
    name: "sell-1"
    display-name: "Sell 64 iron ingots"
    description: "Sell 64 iron ingots"
    thumbnail: IRON_INGOT
    goal: 64
```

***

### `ENTITY_DAMAGE`

This quest involves dealing a certain amount of damage to entities. It is ideal for combat quests without the necessity to kill. You do not need to specify the type of entity, here the objective is to make degats.

**Example usage:**

```yml
quests:
  - type: ENTITY_DAMAGE
    name: "entity-damage-1"
    display-name: "Make 600 of degats to entities"
    description: "Make 600 of degats to entities"
    thumbnail: IRON_SWORD
    goal: 600
```

***

### `EXPERIENCE_GAIN`

The player must gain a certain amount of experience. This can be earned by mining, breeding, crafting, etc.&#x20;

**Example usage:**

```yml
quests:
  - type: EXPERIENCE_GAIN
    name: "experience-1"
    display-name: "Gain 600 of experience"
    description: "Gain 600 of experience"
    thumbnail: STONE
    goal: 600
```

***

### `HATCHING`

This quest involves hatching eggs, such as turtle eggs. It encourages players to take care of animals in the game.

**Example usage:**

```yml
quests:
  - type: HATCHING
    name: "hatching-1"
    display-name: "Use 16 eggs"
    description: "Use 16 eggs"
    thumbnail: EGG
    goal: 16
```

***

### `ITEM_BREAK`

This quest requires the player to break an item (e.g., breaking a pickaxe by using it). It can be used to encourage players to fully utilize their tools.

**Example usage:**

```yml
quests:
  - type: ITEM_BREAK
    actions:
      - material: DIAMOND_SWORD
    name: "item-break-1"
    display-name: "Break a diamond sword"
    description: "Break a diamond sword"
    goal: 1
```

***

### `ITEM_MENDING`

The player must repair an item, either with the Mending enchantment or using an anvil. This encourages players to take care of their equipment.

**Example usage:**

```yml
quests:
  - type: ITEM_MENDING
    actions:
      - material: DIAMOND_SWORD
    name: "item-break-1"
    display-name: "Recover 600 of durability"
    description: "Recover 600 of durability on your diamond sword"
    goal: 600
```

***

### `ITEM_CONSUME`

This quest checks when a player eats an item.

**Example usage:**

```yaml
quests:
  - type: ITEM_CONSUME
    actions:
      - material: SUSPICIOUS_STEW
    name: "item-break-1"
    display-name: "Eat one suspicious stew"
    description: "Eat one suspicious stew"
    goal: 1
```

***

### `RESURRECT`

This quest involves resurrecting an animal or a player, often using a Totem of Undying.

**Example usage:**

```yml
quests:
  - type: RESURRECT
    name: "resurrect-1"
    display-name: "Come back to life"
    description: "Come back to life 8 times"
    thumbnail: STONE
    goal: 8
```

***

### `JOB_LEVEL`

The player must reach a certain level in a job. Only with zJobs for the moment.

**Example usage:**

```yml
quests:
  - type: JOB_LEVEL
    actions:
      - job: miner
    name: "job-1"
    display-name: "Achieve level 50 of the minor job"
    description: "Achieve level 50 of the minor job"
    goal: 50
```

***

### `JOB_PRESTIGE`

The player must reach a certain prestige in a job. Only with zJobs for the moment

**Example usage:**

```yml
quests:
  - type: JOB_PRESTIGE
    actions:
      - job: miner
    name: "job-2"
    display-name: "Achieve prestige 1 of the minor job"
    description: "Achieve prestige 1 of the minor job"
    goal: 1
```

***

### `SMITHING`

The player must craft an item at the smithing table.

**Example usage:**

```yaml
quests:
  - type: SMITHING
    actions:
      - material: NETHERITE_HELMET
    name: "smithing-1"
    display-name: "Craft a netherite helmet"
    description: "Craft a netherite helmet"
    goal: 1
```

***

### `ISLAND`

The player must create or own an island, which only works with the SuperiorSkyBlock2 plugin.

**Example usage:**

```yaml
quests:
  - type: ISLAND
    name: "island-1"
    display-name: "Create an island"
    description: "Create an island"
    goal: 1
```

***

### `COMMAND`

The player must execute a command.

**Example usage:**

```yaml
quests:
  - type: COMMAND
    actions:
      - commands:
          - "home"
          - "homes"
    name: "cmd-1"
    display-name: "Execute /home"
    description: "Execute /home"
    goal: 1
```

***

### `CUBOID`

Allows checking if the player is present within a cuboid.

**Example usage:**

```yaml
quests:
  - type: CUBOID
    actions:
      - cuboid: "world,401,2,-20,461,10,20"
    name: "cuboid-1"
    display-name: "Find the auction house"
    description: "Find the auction house"
    goal: 1
```

The cuboid is composed of the `world name`, a first set of coordinates (`x`, `y`, `z`), and a second set of coordinates (`x2`, `y2`, `z2`).

***

### `CUSTOM`

Allows checking for a specific element related to your plugins. For example, you can verify if a player is interacting with a villager. You must use the API.\
The custom type will take a string data input and verify if the data is correct.

**Example usage:**

```yaml
quests:
  - type: CUSTOM
    actions:
      - data: "citizen:cube"
    name: "custom-1"
    display-name: "Talk to Cube"
    description: "Talk to Cube"
    goal: 1
```

**API example:**

```java
QuestsPlugin questsPlugin = (QuestsPlugin) Bukkit.getServer().getPluginManager().getPlugin("zQuests");
int result = questsPlugin.getQuestManager().handleQuests(player.getUniqueId(), QuestType.CUSTOM, 1, "citizen:cube");
if (result > 0) return;
```

In this example, we will verify the CUSTOM type quest using the citizen's name as a parameter. We then retrieve the number of times a quest has been updated. For instance, if your player clicks on a villager and activates a quest, no action should be taken.

***

### `INVENTORY_OPEN`

Check the inventories opened by the player. Works only with zMenu.

**Example usage:**

```yaml
quests:
  - type: INVENTORY_OPEN
    actions:
      - inventories:
          - "island_shop"
    name: "inventory-1"
    display-name: "Open Island Shop"
    description: "Open Island Shop"
    goal: 1
```

***

### `INVENTORY_CONTENT`

Allows checking the contents of the player's inventory and removing items. You can use a material or a material tag. You must also specify the name of the citizen the player needs to interact with. You must either use the API to execute this action or use the command `/q progress-inventory <player> <citizen name>`.

**Example usage:**

```yaml
quests:
  - type: INVENTORY_CONTENT
    actions:
      - tag: OAK_LOGS
        citizen-name: cube
    name: "inventory-2"
    display-name: "Deliver 500 oak logs to Cube"
    description: "Deliver 500 oak logs to Cube"
    goal: 500
```

**API example:**

```java
QuestsPlugin questsPlugin = (QuestsPlugin) Bukkit.getServer().getPluginManager().getPlugin("zQuests");
int result = questsPlugin.getQuestManager().handleInventoryQuests(new InventoryContent(player, "cube"));
if (result > 0) return;
```

***

### `SHEAR`

Allows checking when a player shears an entity.

**Example usage:**

```yaml
quests:
  - type: SHEAR
    actions:
      - entity: SHEEP
    name: "shear-1"
    display-name: "Shear 20 sheep"
    description: "Shear 20 sheep"
    goal: 20
```















