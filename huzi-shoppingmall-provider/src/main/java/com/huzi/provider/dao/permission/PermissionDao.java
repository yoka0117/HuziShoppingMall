package com.huzi.provider.dao.permission;


import com.huzi.domain.permission.Permission;
import com.huzi.domain.permission.UserPermission;

public interface PermissionDao {


    //添加权限
    int addPermission(Permission permission);



    //废除权限（根据权限id）
    int discardPermission(Permission permission);




    //给用户分配权限
    int setPermissionsForUser(UserPermission userPermission);


    int deletePermissionsForUser(UserPermission userPermission);
}
