package com.huzi.domain.permission;


import java.io.Serializable;
import java.util.Date;

public class Permission implements Serializable {
    private Integer permissionId;
    private String permissionCode;
    private String permissionDescribe;
    private Date permissionCreationTime;
    private Date permissionUpdateTime;
    private String permissionState;


    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionDescribe() {
        return permissionDescribe;
    }

    public void setPermissionDescribe(String permissionDescribe) {
        this.permissionDescribe = permissionDescribe;
    }

    public Date getPermissionCreationTime() {
        return permissionCreationTime;
    }

    public void setPermissionCreationTime(Date permissionCreationTime) {
        this.permissionCreationTime = permissionCreationTime;
    }

    public Date getPermissionUpdateTime() {
        return permissionUpdateTime;
    }

    public void setPermissionUpdateTime(Date permissionUpdateTime) {
        this.permissionUpdateTime = permissionUpdateTime;
    }

    public String getPermissionState() {
        return permissionState;
    }

    public void setPermissionState(String permissionState) {
        this.permissionState = permissionState;
    }
}
