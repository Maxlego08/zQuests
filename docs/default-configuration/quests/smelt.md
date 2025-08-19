# smelt

```yaml
quests:
  - type: SMELT # The type of the quest
    actions: # Actions to be performed in the quest
      - material: IRON_INGOT # Type of material to be smelted
    name: "smelt-iron-1" # The name must be unique and must never change
    display-name: "Smelt Iron" # The name of the quest to be displayed in-game
    description: "Smelt 8 iron ingots" # The description of the quest
    thumbnail: IRON_INGOT # The material to be displayed in-game
    goal: 8 # The goal to be achieved
    auto-accept: true # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have smelted &68 iron ingots&f!"

  - type: SMELT # The type of the quest
    actions: # Actions to be performed in the quest
      - material: STONE # Type of material to be smelted
    name: "smelt-stone-1" # The name must be unique and must never change
    display-name: "Smelt Stone" # The name of the quest to be displayed in-game
    description: "Smelt 16 stone" # The description of the quest
    thumbnail: STONE # The material to be displayed in-game
    goal: 16 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have smelted &616 stone&f!"
```
