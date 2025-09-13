# config.yml

```yaml
# Enables detailed information display in the console.
# If you encounter an issue, enable this option and send the errors to support.
enable-debug: true

# Enables execution time debugging to measure plugin performance.
# Useful for identifying bottlenecks and optimizing performance.
enable-debug-time: true

# Storage:
# SQLITE - For the launch of the plugin only.
# MYSQL - Allows creating a simple connection to the database, only for small servers.
# HIKARICP - HikariCP is a fast and lightweight JDBC connection pool. It optimizes database connections, ensuring quick acquisition and low latency. This improves performance and reliability, making it ideal for high-demand applications.
#
# We advise you to use MYSQL or HIKARICP, the SQLITE storage is only there to install the plugin and do some tests, not all features are available with SQLITE yet.
# The plugin will work, but some features like sanctions update when launching the plugin will not work.
# This will be fixed in future plugin updates
storage-type: SQLITE

# Configuration of your database, it is recommended to use the database to store your data.
# JSON does not support everything
database-configuration:
  # The prefix that will be applied to all tables, if you have several plugins with the same database you must have one.
  # It is advisable not to change this value
  table-prefix: "zquests_"
  # IP Address of the machine the database is hosted on
  host: 192.168.10.10
  # Port of the database, by default, MYSQL's port is 3306
  port: 3306
  # Database username
  user: homestead
  # Database password
  password: 'secret'
  # Database
  database: zquests
  # Enable of not the SQL debug mode
  debug: true

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

# Main command aliases:
# Defines the alternative aliases for the main plugin command.
# These aliases make it easier for players to access plugin commands using different variations.
main-command-aliases:
  - quêtes
  - quête
  - quests
  - quest
  - rangs
  - rankup
  - q

# Name of the file to open the inventory with the main command
main-command-inventory-name: "quests"

# Permission that the player must have to change the page with the main command
main-command-page:
  - permission: "quests.page.2"
    inventory: "quests"
    page: 2
    priority: 1
  - permission: "quests.page.3"
    inventory: "quests"
    page: 3
    priority: 2

# Lore line placeholders:
# active: The format for an active quest's progress in the lore, including placeholders for the progress bar, progress, and goal.
# complete: The format for a completed quest in the lore, indicating completion with a checkmark symbol.
lore-line-placeholder:
  active: "%progress-bar% &8- &6%progress%&8/&f%goal% &c✘"
  complete: "%progress-bar% &8- &6%progress%&8/&f%goal% &a✔"

# Custom rewards:
# Allows configuration of specific rewards when certain quests are completed.
# quests: A list of quests that need to be completed to trigger the reward.
# actions: Defines the reward action that will be performed. In this case, it sends a congratulatory message to the player.
custom-rewards:
  - quests:
      - "block-break-1"
      - "block-break-2"
      - "block-break-3"
    actions:
      - type: message
        messages:
          - "&7You have just completed 3 quests! Congratulations!"

# Rewards that are executed for all quests.
global-rewards:
  - type: message
    messages:
      - "&fYou have broken &7%name%&f blocks!" # Placeholder : %name%, %description% and %goal%

# Allows grouping multiple quests to display them in placeholders.
# Placeholders :
# %zquests_group_name_<group id>% - Returns the display name of the group
# %zquests_group_count_<group id>% - Displays the number of quests in the group
# %zquests_group_finish_<group id>% - Displays the number of completed quests in the group
# %zquests_group_percent_<group id>% - Displays the percentage of completed quests in the group
# %zquests_group_total_percent_<group id>% - Displays the percentage of completed and ongoing quests, providing a more precise progress value than the placeholder above
quests-groups:
  block-break: # Group identifier
    display-name: "Block Break" # Display name of the group
    quests: # Quests present in the group
      - "block-break-1"
      - "block-break-2"
      - "block-break-3"

# The name of the group by default.
global-group-display-name: "Global"

# Manages quest-related events
events:
  - event: QuestStartEvent # Triggered when a quest begins
    enabled: true # Indicates if the event is active
    update-scoreboard: false # Determines if the scoreboard should be updated when this event occurs

  - event: QuestProgressEvent # Triggered when quest progress is made
    enabled: true # Indicates if the event is active
    update-scoreboard: false # Determines if the scoreboard should be updated when this event occurs

  - event: QuestCompleteEvent # Triggered when a quest is completed
    enabled: true # Indicates if the event is active
    update-scoreboard: false # Determines if the scoreboard should be updated when this event occurs

  - event: QuestFavoriteChangeEvent # Triggered when a quest's favorite status changes
    enabled: true # Indicates if the event is active
    update-scoreboard: false # Determines if the scoreboard should be updated when this event occurs

  - event: QuestPostProgressEvent # Triggered after quest progress is made
    enabled: true # Indicates if the event is active
    update-scoreboard: false # Determines if the scoreboard should be updated when this event occurs

  - event: QuestUserLoadEvent # Triggered when a user is loaded
    enabled: true # Indicates if the event is active
    update-scoreboard: false # Determines if the scoreboard should be updated when this event occurs

# The format of dates used for placeholders that will display a date
date-format: "dd/MM/yyyy HH:mm:ss"

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

