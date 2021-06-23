package com.huzi.service.permission;

import com.huzi.domain.permission.*;

import java.util.List;

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
    UserPermission selectUserPermissionByUserIdAndPermissionCode(Integer userId,String permissionCode);




    //根据用户id，查找所属角色
    List<UserRole> selectUserRoleByUserId(Integer userId);



    //通过角色id和权限code查找是否有此映射对象
    RolePermission selectRolePermissionByRoleIdAndPermissionCode(Integer roleId , String  permissionCode);
}
