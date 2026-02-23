# 1.0.0.4

- Update to last zMenu version

# 1.0.0.3

- Update to last zMenu and Sarah version
- Create placeholders ``%zquests_complete_<quest_id>%`` and ``%zquests_active_<quest_id>%``

# 1.0.0.2

- Fixed ``force-conditions`` behavior
- Resolved ConcurrentModificationException occurrences
- Added safeguards to prevent concurrent modifications during quest completion
- Added validation to ensure a quest cannot be registered if it already exists

# 1.0.0.1

- Update to Sarah 1.20, added MariaDB support
- Fixed craft listener
- Added force complete conditions
- Improved performance