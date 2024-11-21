package fr.maxlego08.quests.zcore.enums;

public enum Permission {

    ZQUESTS_USE, ZQUESTS_RELOAD,

    ZQUESTS_START, ZQUESTS_DELETE, ZQUESTS_COMPLETE, ZQUESTS_DELETE_ALL, ZQUESTS_SET_PROGRESS;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String asPermission() {
        return permission;
    }

}
