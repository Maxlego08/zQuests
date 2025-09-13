# 🛠 Usage

## Accessing managers

Use Bukkit's `ServicesManager` to obtain the different API managers exposed by zQuests.

```java
QuestManager questManager = Bukkit.getServicesManager().load(QuestManager.class);
HologramManager hologramManager = Bukkit.getServicesManager().load(HologramManager.class);
WayPointManager wayPointManager = Bukkit.getServicesManager().load(WayPointManager.class);

if (questManager != null) {
    // interact with quests
}
```

## Listening to quest events

zQuests publishes several events to let other plugins react to quest progress.

```java
public class QuestListener implements Listener {

    @EventHandler
    public void onQuestComplete(QuestCompleteEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            player.sendMessage("Completed: " +
                event.getActiveQuest().getQuest().getDisplayName());
        }
    }
}
```

Register your listener like any other Bukkit listener to start receiving callbacks.

## Custom quest actions

Trigger your own quest logic by sending custom actions through the API. This opens the door
to crafting entirely new quest types inside your plugins.

```java
// questsPlugin is a reference to the zQuests plugin instance
questsPlugin.getQuestManager().handleQuests(player.getUniqueId(), QuestType.CUSTOM, 1, "<your info>");
```

Configure the corresponding quest to expect your custom data:

```yaml
quests:
  - type: CUSTOM
    actions:
      - data: '<your info>'
```

By interpreting the `data` field however you like, you can implement virtually any quest behaviour.
