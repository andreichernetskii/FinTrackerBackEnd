package com.example.finmanagerbackend.not_for_use;

public enum ApplicationUserPermissionNotUse {
    USER_READ( "user:read" ),
    USER_WRITE( "user:write" ),
    DATA_READ( "data:read" ),
    DATA_WRITE( "data:write" );

    private final String permission;

    ApplicationUserPermissionNotUse( String permission ) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
