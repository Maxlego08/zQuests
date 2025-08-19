# farming

```yaml
quests:
  - type: FARMING # The type of the quest
    actions:
      - material: WHEAT
    name: "farming-wheat-1" # The name must be unique and must never change
    display-name: "Wheat Breaker" # The name of the quest to be displayed in-game
    description: "Break 500 wheat blocks" # The description of the quest
    thumbnail: WHEAT # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: true # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &7500 wheat&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"

  - type: FARMING # The type of the quest
    actions:
      - material: CARROTS
    name: "farming-carrot-1" # The name must be unique and must never change
    display-name: "Carrot Breaker" # The name of the quest to be displayed in-game
    description: "Break 500 carrot blocks" # The description of the quest
    thumbnail: CARROT # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &7500 carrot&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"

  - type: FARMING # The type of the quest
    actions:
      - material: POTATOES
    name: "farming-potato-1" # The name must be unique and must never change
    display-name: "Potato Breaker" # The name of the quest to be displayed in-game
    description: "Break 500 potato blocks" # The description of the quest
    thumbnail: POTATO # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &7500 potato&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"


  - type: FARMING # The type of the quest
    actions:
      - material: MELON
    name: "farming-melon-1" # The name must be unique and must never change
    display-name: "Melon Breaker" # The name of the quest to be displayed in-game
    description: "Break 10 melon" # The description of the quest
    thumbnail: MELON # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &710 melon&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"

  - type: FARMING # The type of the quest
    actions:
      - material: PUMPKIN
    name: "farming-pumpkin-1" # The name must be unique and must never change
    display-name: "Pumpkin Breaker" # The name of the quest to be displayed in-game
    description: "Break 500 pumpkin" # The description of the quest
    thumbnail: PUMPKIN # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &710 pumpkin&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"

  - type: FARMING # The type of the quest
    actions:
      - material: CACTUS
    name: "farming-cactus-1" # The name must be unique and must never change
    display-name: "Cactus Breaker" # The name of the quest to be displayed in-game
    description: "Break 10 cactus" # The description of the quest
    thumbnail: CACTUS # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &710 cactus&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"

  - type: FARMING # The type of the quest
    actions:
      - material: SUGAR_CANE
    name: "farming-sugar-cane-1" # The name must be unique and must never change
    display-name: "Sugar Cane Breaker" # The name of the quest to be displayed in-game
    description: "Break 10 sugar cane" # The description of the quest
    thumbnail: SUGAR_CANE # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have broken &710 sugar cane&f !"
      - type: console_command # Executing a command
        commands:
          - "give %player% diamond 5"
```
