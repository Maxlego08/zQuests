# 📖 Inventories

By default, when the player goes to **/quest**, the inventory `quests.yml` will open.

## Buttons

### ZQUESTS\_COMPLETE

Check if a quest list is complete

#### Example:

```yaml
items:
  quest-example:
    type: ZQUESTS_COMPLETE
    slot: 10
    quests:
      - "block-break-1"
      - "block-break-2"
      - "block-break-3"
    item:
      material: PAPER
      name: "&aCongratulations!"
      lore:
        - "&8You have completed all the block break quests"
    else:
      item:
        material: PAPER
        name: "&cYou are not finished yet!"
        lore:
          - "&7You must complete the quests!"
```

## Actions

The zMenu action system allows to integrate new actions specific to a plugin.

### START\_QUEST

This action starts a new quest. You can add it as a reward when a player completes a quest to automatically start the next one. You must specify the name of the quest.

#### Example:

```yaml
actions:
  - type: START_QUEST
    quest: "block-break-3"
```

