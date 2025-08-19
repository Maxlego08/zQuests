# 📰 PlaceHolders

<table data-full-width="true"><thead><tr><th>Placeholder</th><th>Description</th></tr></thead><tbody><tr><td><code>%zquests_name_&#x3C;quest-name>%</code></td><td>Displays the name of the quest. Returns "Unknown" if the quest ID is not valid.</td></tr><tr><td><code>%zquests_description_&#x3C;quest-name>%</code></td><td>Displays the description of the quest. Returns "Unknown" if the quest ID is not valid.</td></tr><tr><td><code>%zquests_thumbnail_&#x3C;quest-name>%</code></td><td>Displays the thumbnail material for the quest. Returns "PAPER" if the quest ID is not valid.</td></tr><tr><td><code>%zquests_type_&#x3C;quest-name>%</code></td><td>Displays the type of the quest (e.g., BLOCK_BREAK, BLOCK_PLACE). Returns "Unknown" if invalid.</td></tr><tr><td><code>%zquests_objective_&#x3C;quest-name>%</code></td><td>Displays the objective (goal) of the quest for the given quest ID.</td></tr><tr><td><code>%zquests_is_completed_&#x3C;quest-name>%</code></td><td>Indicates whether the quest is completed by the player (<code>true</code> or <code>false</code>).</td></tr><tr><td><code>%zquests_is_active_&#x3C;quest-name>%</code></td><td>Indicates whether the quest is active for the player (<code>true</code> or <code>false</code>).</td></tr><tr><td><code>%zquests_progress_bar_&#x3C;quest-name>%</code></td><td>Displays a progress bar representing the player's progress towards completing the quest.</td></tr><tr><td><code>%zquests_progress_&#x3C;quest-name>%</code></td><td>Displays the current progress (e.g., number of blocks broken) of the quest for the player.</td></tr><tr><td><code>%zquests_lore_line_&#x3C;quest-name>%</code></td><td>Displays a formatted line showing the progress towards completing the quest.</td></tr><tr><td><code>%zquests_group_name_&#x3C;group id>%</code></td><td>Returns the display name of the group</td></tr><tr><td><code>%zquests_group_count_&#x3C;group id>%</code></td><td>Displays the number of quests in the group</td></tr><tr><td><code>%zquests_group_finish_&#x3C;group id>%</code></td><td>Displays the number of completed quests in the group</td></tr><tr><td><code>%zquests_group_percent_&#x3C;group id>%</code></td><td>Displays the percentage of completed quests in the group</td></tr><tr><td><code>%zquests_group_total_percent_&#x3C;group id>%</code></td><td>Displays the percentage of completed and ongoing quests, providing a more precise progress value than the placeholder above</td></tr><tr><td><code>%zquests_favorite_quests%</code></td><td>Displays the list of favorite quests</td></tr></tbody></table>

In the [**config.yml**](../default-configuration/config.yml.md) file you can configure the placeholder `%zquests_lore_line_<quest-name>%`, `%zquests_favorite_quests%` and `%zquests_progress_bar_<quest-name>%.`&#x20;

```yaml
# Progress bar settings:
# icon: The character used to display the completed progress.
# not-completed-icon: The character used to display the uncompleted progress.
# progress-color: The color of the progress bar that is already completed.
# color: The color of the progress bar for sections not yet completed.
# size: The total length of the progress bar, representing the complete goal.
progress-bar:
  icon: '|'
  not-completed-icon: '|'
  progress-color: "#0ff216"
  color: "#828282"
  size: 30
  
# Lore line placeholders:
# active: The format for an active quest's progress in the lore, including placeholders for the progress bar, progress, and goal.
# complete: The format for a completed quest in the lore, indicating completion with a checkmark symbol.
lore-line-placeholder:
  active: "%progress-bar% &8- &6%progress%&8/&f%goal% &c✘"
  complete: "%progress-bar% &8- &6%progress%&8/&f%goal% &a✔"  
  
# Allows creating a placeholder that will group multiple favorite quests.
# This can be used in a scoreboard or an inventory.
placeholder-favorite:
  # The number of favorite quests to display
  limit: 3
  # The message to display when there are no favorite quests
  empty: "&cNo favorite quests"
  # The result for each quest, you can use the following placeholders:
  # %quest-name% - The name of the quest
  # %quest-display-name% - The display name of the quest
  # %quest-description% - The description of the quest
  # %quest-thumbnail% - The icon of the quest
  # %quest-type% - The type of the quest
  # %quest-objective% - The objective of the quest
  # %quest-lore-line% - The placeholder lore-line-placeholder
  # %quest-progress-bar% - The progress bar of the quest
  # %quest-percent% - The percentage of the quest's progress
  # %quest-progress% - The progress of the quest
  # %quest-global-group-name% - The global group name of the quest
  result: "&f%quest-description%\n&8%quest-display-name%\n#fcd600%quest-progress%&8/&f%quest-objective%"
  # The string that will be placed between two quests
  between: "\n\n"  
```
