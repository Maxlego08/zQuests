# 🎁 Rewards

This section explains the configuration for custom rewards and global rewards, allowing for greater customization of the reward system when players complete quests.

```yml
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
```

## `custom-rewards`

Custom rewards allow you to configure specific rewards that are given when a player completes certain quests.

* **`quests` (Required)**: A list of quest identifiers that must be completed to trigger the reward. Each identifier must match the unique `name` of a quest.
* **`actions` (Required)**: Defines the actions that will be taken when the reward is triggered. Actions can include sending messages, giving items, executing commands, etc.

**Example:**

```yml
custom-rewards:
  - quests:
      - "block-break-1"
      - "block-break-2"
      - "block-break-3"
    actions:
      - type: message
        messages:
          - "&7You have just completed 3 quests! Congratulations!"
```

#### **`quests`**

Specifies the list of quests that need to be completed to trigger the reward. These should be the unique identifiers (`name`) of the quests.

**Example:**

```yml
quests:
  - "block-break-1"
  - "block-break-2"
  - "block-break-3"
```

#### **`actions`**

Defines the rewards that will be given once the specified quests are completed. The following types of actions can be used:

{% hint style="info" %}
You can use all [actions](https://docs.zmenu.dev/configurations/buttons/actions) of zMenu
{% endhint %}

**Example:**

```yml
actions:
  - type: message
    messages:
      - "&7You have just completed 3 quests! Congratulations!"
```

## `global-rewards`

Global rewards are rewards that are triggered for all quests. They are applied universally whenever a quest is completed.

{% hint style="info" %}
You can use all [actions](https://docs.zmenu.dev/configurations/buttons/actions) of zMenu
{% endhint %}

**Example:**

```yml
global-rewards:
  - type: message
    messages:
      - "&fYou have broken &7%name%&f blocks!" # Placeholder : %name%, %description%, and %goal%
```

## Notes

* **`custom-rewards`** can be configured in any quest configuration file, allowing for flexibility in specifying quest-specific rewards.
* Ensure that each quest in the `quests` list matches a valid quest `name` defined elsewhere in your configuration.
* **`global-rewards`** are applied to all quests, making them useful for giving generic rewards or acknowledgments for quest completions.
* **Placeholders** such as `%name%`, `%description%`, and `%goal%` can make reward messages more dynamic and personalized.
