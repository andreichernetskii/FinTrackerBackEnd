package com.example.finmanagerbackend.application_user;

public enum ApplicationUserPermission {
    USER_READ( "user:read" ),
    USER_WRITE( "user:write" ),
    DATA_READ( "data:read" ),
    DATA_WRITE( "data:write" );

    private final String permission;

    ApplicationUserPermission( String permission ) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
