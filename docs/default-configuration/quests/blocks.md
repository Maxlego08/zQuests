# blocks

```yaml
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
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &7500 stone&f blocks!"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"

  - type: BLOCK_BREAK # The type of the quest
    actions:
      - material: GRANITE
      - material: ANDESITE
      - material: DIORITE
    name: "block-break-2" # The name must be unique and must never change
    display-name: "Granite Breaker" # The name of the quest to be displayed in-game
    description: "Break 300 granite blocks" # The description of the quest
    thumbnail: GRANITE # The material to be displayed in-game
    goal: 300 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &7300 granite&f blocks!"
      - type: console_command # Executing a command
        commands:
          - "give %player% emerald 3"

  - type: BLOCK_BREAK # The type of the quest
    actions:
      - material: SANDSTONE
      - material: RED_SANDSTONE
    name: "block-break-3" # The name must be unique and must never change
    display-name: "Sandstone Breaker" # The name of the quest to be displayed in-game
    description: "Break 200 sandstone blocks" # The description of the quest
    thumbnail: SANDSTONE # The material to be displayed in-game
    goal: 200 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &7200 sandstone&f blocks!"
      - type: console_command # Executing a command
        commands:
          - "give %player% gold_ingot 4"

  - type: BLOCK_BREAK # The type of the quest
    actions:
      - tag: LOGS
    name: "log-breaker" # The name must be unique and must never change
    display-name: "Log Breaker" # The name of the quest to be displayed in-game
    description: "Cut 20 logs" # The description of the quest
    thumbnail: OAK_LOG # The material to be displayed in-game
    goal: 20 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &720 logs&f blocks!"
      - type: console_command # Executing a command
        commands:
          - "give %player% emerald 3"

  - type: BLOCK_BREAK # The type of the quest
    actions:
      - tag: MINEABLE_PICKAXE
    name: "miner" # The name must be unique and must never change
    display-name: "Miner" # The name of the quest to be displayed in-game
    description: "Mine 300 blocks" # The description of the quest
    thumbnail: DIAMOND_PICKAXE # The material to be displayed in-game
    goal: 300 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have mined &7300 blocks&f blocks!"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 4"
```
