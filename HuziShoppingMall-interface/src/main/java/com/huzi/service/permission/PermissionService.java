package com.huzi.service.permission;

import com.huzi.domain.permission.Permission;
import com.huzi.domain.permission.UserPermission;

public interface PermissionService {

    //添加权限管理
    String addPermission(Permission permission);


    //废弃权限管理
    int discardPermission(Integer permissionId);

    //根据permissionId查permission
    Permission selectPermissionByPermissionId(Integer permissionId);






    //给用户增加权限
    String setPermissionsForUser(String userID, String permissionID);
    //删除用户权限
    String deletePermissionsForUser(String userID, String permissionID);



    //根据用户id查用户权限
    UserPermission selectUserPermissionByUserId(Integer userId);

}
