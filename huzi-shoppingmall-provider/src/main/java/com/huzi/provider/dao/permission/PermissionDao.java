package com.huzi.provider.dao.permission;


import com.huzi.domain.permission.Permission;
import com.huzi.domain.permission.UserPermission;

public interface PermissionDao {


    //添加权限（新建）
    int addPermission(Permission permission);

    //废除权限（根据权限id）
    int discardPermission(Permission permission);

    //根据permissionId查permission
    Permission selectPermissionByPermissionId(Integer permissionId);




    //给用户增加权限（新建添加操作）
    int setPermissionsForUser(UserPermission userPermission);

    //删除用户权限
    int deletePermissionsForUser(UserPermission userPermission);


    //通过userId查找权限
    UserPermission selectUserPermissionByUserId(Integer userId);


}
