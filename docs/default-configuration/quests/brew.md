# brew

```yaml
quests:
  - type: BREW # The type of the quest
    actions: # Actions to be performed in the quest
      - potion-type: SWIFTNESS # Type of potion to be brewed
    name: "brew-swiftness-3" # The name must be unique and must never change
    display-name: "Brew 3 swiftness" # The name of the quest to be displayed in-game
    description: "Make 3 swiftness potions" # The description of the quest
    thumbnail: POTION # The material to be displayed in-game
    goal: 3 # The goal to be achieved
    auto-accept: true # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have brewed &73 swiftness potions&f!"

  - type: BREW # The type of the quest
    actions: # Actions to be performed in the quest
      - potion-type: LONG_FIRE_RESISTANCE # Type of potion to be brewed
        potion-material: SPLASH_POTION # The material of the potion (SPLASH_POTION or POTION)
    name: "brew-long-fire-resistance-3" # The name must be unique and must never change
    display-name: "Brew 3 Long Fire Resistance Splash Potions" # The name of the quest to be displayed in-game
    description: "Make 3 long fire resistance splash potions" # The description of the quest
    thumbnail: POTION # The material to be displayed in-game
    goal: 3 # The goal to be achieved
    auto-accept: false # If the quest should start automatically
    rewards: # The rewards
      - type: message # Sending a message
        messages:
          - "&fYou have brewed &73 long fire resistance splash potions&f!"
```
