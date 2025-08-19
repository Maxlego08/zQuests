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
