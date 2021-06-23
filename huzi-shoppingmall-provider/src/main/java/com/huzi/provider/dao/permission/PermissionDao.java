package com.huzi.provider.dao.permission;


import com.huzi.domain.permission.*;

import java.util.List;

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
    UserPermission selectUserPermissionByUserIdAndPermissionCode(Integer userId,String permissionCode);





    //角色相关
    Role selectRole();

    UserRole selectUserRole();

    RolePermission selectRolePermission();






    //根据用户id，查找所属角色
    List<UserRole> selectUserRoleByUserId(Integer userId);


    ////通过角色id和权限code查找是否有此映射对象
    RolePermission selectRolePermissionByRoleIdAndPermissionCode(Integer roleId, String permissionCode);
}
