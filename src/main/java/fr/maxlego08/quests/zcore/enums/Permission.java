package fr.maxlego08.quests.zcore.enums;

public enum Permission {

    ZQUESTS_USE, ZQUESTS_RELOAD,

    ;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String asPermission() {
        return permission;
    }

}
