# fish

```yaml
quests:
  - type: FISHING # The type of the quest
    actions: # Actions to be performed in the quest
      - material: COD # Type of fish to be caught
    name: "fishing-cod-1" # The name must be unique and must never change
    display-name: "Fishing Cod" # The name of the quest to be displayed in-game
    description: "Catch 8 cod" # The description of the quest
    thumbnail: COD # The material to be displayed in-game
    goal: 8 # The goal to be achieved
    auto-accept: true # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have caught &68 cod&f!"

  - type: FISHING # The type of the quest
    actions: # Actions to be performed in the quest
      - material: SALMON # Type of fish to be caught
    name: "fishing-salmon-1" # The name must be unique and must never change
    display-name: "Fishing Salmon" # The name of the quest to be displayed in-game
    description: "Catch 10 salmon" # The description of the quest
    thumbnail: SALMON # The material to be displayed in-game
    goal: 10 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have caught &610 salmon&f!"

  - type: FISHING # The type of the quest
    actions: # Actions to be performed in the quest
      - material: PUFFERFISH # Type of fish to be caught
    name: "fishing-pufferfish-1" # The name must be unique and must never change
    display-name: "Fishing Pufferfish" # The name of the quest to be displayed in-game
    description: "Catch 5 pufferfish" # The description of the quest
    thumbnail: PUFFERFISH # The material to be displayed in-game
    goal: 5 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have caught &65 pufferfish&f!"
```
