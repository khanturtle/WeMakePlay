package com.wemakeplay.wemakeplay.domain.user.entity;

public enum UserRoleEnum {

    USER("ROLE_USER"),  // 사용자 권한
    ADMIN("ROLE_ADMIN"),  // 관리자 권한
    OWNER("ROLE_OWNER"); // 방장 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }
}
