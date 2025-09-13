# entities

```yaml
quests:
  - type: ENTITY_KILL # The type of the quest
    actions: # List of entities to be considered
      - entity: SKELETON
    name: "entity-skeleton-1" # The name must be unique and must never change
    display-name: "Skeleton Killer" # The name of the quest to be displayed in-game
    description: "Kill 10 Skeleton" # The description of the quest
    thumbnail: BONE # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: true # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have killed &710 skeleton&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"

  - type: ENTITY_KILL # The type of the quest
    actions: # List of entities to be considered
      - entity: ZOMBIE
    name: "entity-zombie-1" # The name must be unique and must never change
    display-name: "Zombie Slayer" # The name of the quest to be displayed in-game
    description: "Kill 15 Zombies" # The description of the quest
    thumbnail: ROTTEN_FLESH # The material to be displayed in-game
    goal: 15 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have killed &715 zombies&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% iron_ingot 3"

  - type: ENTITY_KILL # The type of the quest
    actions: # List of entities to be considered
      - entity: CREEPER
    name: "entity-creeper-1" # The name must be unique and must never change
    display-name: "Creeper Crusher" # The name of the quest to be displayed in-game
    description: "Kill 5 Creepers" # The description of the quest
    thumbnail: GUNPOWDER # The material to be displayed in-game
    goal: 5 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have killed &75 creepers&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% tnt 2"

  - type: ENTITY_KILL # The type of the quest
    actions: # List of entities to be considered
      - entity: ENDERMAN
    name: "entity-enderman-1" # The name must be unique and must never change
    display-name: "Enderman Hunter" # The name of the quest to be displayed in-game
    description: "Kill 3 Endermen" # The description of the quest
    thumbnail: ENDER_PEARL # The material to be displayed in-game
    goal: 3 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have killed &73 endermen&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% ender_pearl 3"

  - type: ENTITY_KILL # The type of the quest
    actions: # List of entities to be considered
      - entity: COW
    name: "entity-cow-1" # The name must be unique and must never change
    display-name: "Cow Butcher" # The name of the quest to be displayed in-game
    description: "Kill 20 Cows" # The description of the quest
    thumbnail: BEEF # The material to be displayed in-game
    goal: 20 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have killed &720 cows&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% leather 10"
```
