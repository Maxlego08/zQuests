package fr.maxlego08.quests.zcore.enums;

public enum Permission {

    ZQUESTS_USE, ZQUESTS_RELOAD,

    ZQUESTS_START,
    ZQUESTS_START_ALL,
    ZQUESTS_DELETE,
    ZQUESTS_COMPLETE, ZQUESTS_DELETE_ALL, ZQUESTS_SET_PROGRESS,
    ZQUESTS_HELP, ZQUESTS_ADD_PROGRESS, ZQUESTS_COMPLETE_ALL,
    ZQUESTS_PROGRESS_INVENTORY,
    ZQUESTS_SET_FAVORITE,
    ZQUESTS_SET_FAVORITE_LIMIT,
    ZQUESTS_SET_FAVORITE_TYPE,
    ZQUESTS_RESTART, ZQUESTS_REFRESH_HOLOGRAM,
    ZQUESTS_ADD_FAVORITE,
    ZQUESTS_SHOW;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String asPermission() {
        return permission;
    }

}
