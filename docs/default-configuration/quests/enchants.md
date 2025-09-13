# enchants

```yaml
quests:
  - type: ENCHANT
    actions:
      - enchantment: SHARPNESS
    name: "enchant-sharpness-1"
    display-name: "Enchant Sharpness"
    description: "Enchant 8 items with sharpness"
    thumbnail: ENCHANTED_BOOK
    goal: 8
    auto-accept: true
    rewards:
      - type: message
        messages:
          - "&fYou have enchanted &68 items with sharpness&f!"

  - type: ENCHANT
    actions:
      - minimum-cost: 24
    name: "enchant-cost-1"
    display-name: "Enchant Sharpness"
    description: "Enchant 4 items with a minimum cost of 24 levels"
    thumbnail: ENCHANTED_BOOK
    goal: 4
    auto-accept: false
    rewards:
      - type: message
        messages:
          - "&fYou have enchanted &64 items with a minimum cost of 24 levels&f!"
```
