package com.huzi.provider.serviceImpl.permission;

import com.huzi.common.PermissionState;
import com.huzi.domain.permission.Permission;
import com.huzi.domain.permission.UserPermission;
import com.huzi.provider.dao.permission.PermissionDao;
import com.huzi.service.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    //创建权限
    @Override
    public String addPermission(Permission permission) {
        System.out.println(permission.getPermissionId());
        permission.setPermissionCreationTime(new Date());
        permission.setPermissionState(PermissionState.NORMAL.name());
        Integer returnNum =  permissionDao.addPermission(permission);
        System.out.println(returnNum);
        String result = "";
        if (returnNum > 0){
            result = "权限"+ permission.getPermissionId() + "添加成功" ;
            return result;
        }else {
            result = "权限" +  permission.getPermissionId() + "添加失败!!";
            return result;
        }
    }


    //废弃权限（根据权限id）
    @Override
    public int discardPermission(Integer permissionId) {
        Permission permission  = new Permission();
        permission.setPermissionId(permissionId);
        permission.setPermissionUpdateTime(new Date());
        permission.setPermissionState(PermissionState.DISCARD.name());
        return permissionDao.discardPermission(permission);
    }


    //根据permissionId查permission
    @Override
    public Permission selectPermissionByPermissionId(Integer permissionId) {

        return permissionDao.selectPermissionByPermissionId(permissionId);

    }


    //给用户增加权限
    @Override
    public String setPermissionsForUser(String userID, String permissionID) {

        StringBuffer stringBuffer = new StringBuffer("权限增添失败名单如下：");
        String result = "";
        Boolean tip = true;
        //将字符串转换成String数组
        String[] userIdStr = userID.split(",");
        String[] permissionIdStr = permissionID.split(",");

        //做第二次过滤，防止数组中的个数对应不上。
        if (userIdStr.length != permissionIdStr.length){
            return "权限增加失败，请检查后重新输入";
        }

        //循环取出,循环次数是数组的长度
        for(int a = 0 ; a < userIdStr.length ; a++){
            //将元素取出，转换成正确的数据类型
            Integer userId = Integer.valueOf(userIdStr[a]);
            Integer permissionId = Integer.valueOf(permissionIdStr[a]);
            //装入映射对象中,并添加创建时间
            UserPermission userPermission = new UserPermission();
            userPermission.setUserId(userId);
            userPermission.setPermissionId(permissionId);
            userPermission.setCreationTime(new Date());
            //将对象存入数据库表中
            if (permissionDao.setPermissionsForUser(userPermission) > 0){
                //成功，开始下次循环
                break;
            }else {
                //失败，给提示
                stringBuffer.append("用户"+ userId +",权限" + permissionId +"分配失败;");
                tip = false;
            }
        }
        //分配完毕,检查布尔标记，是否全部成功
        if (tip == true){
            result = "分配成功！";
        }else {
            result = stringBuffer.toString();
        }
        return result;
    }



    //删除用户权限
    @Override
    @Transactional
    public String deletePermissionsForUser(String userID, String permissionID) {

        StringBuffer stringBuffer = new StringBuffer("权限删除失败名单如下：");
        String result = "";
        Boolean tip = true;
        //将字符串转换成String数组
        String[] userIdStr = userID.split(",");
        String[] permissionIdStr = permissionID.split(",");

        //做第二次过滤，防止数组中的个数对应不上。
        if (userIdStr.length != permissionIdStr.length){
            return "用户权限删除失败，个数对不上,请检查后重新输入";
        }

        //循环取出,循环次数是数组的长度
        for(int a = 0 ; a < userIdStr.length ; a++){
            //将元素取出，转换成正确的数据类型
            Integer userId = Integer.valueOf(userIdStr[a]);
            Integer permissionId = Integer.valueOf(permissionIdStr[a]);
            //装入映射对象中
            UserPermission userPermission = new UserPermission();
            userPermission.setUserId(userId);
            userPermission.setPermissionId(permissionId);
            //将对象存入数据库表中
            if (permissionDao.deletePermissionsForUser(userPermission) > 0){
                //删除成功，开始下次循环
                break;
            }else {
                //删除失败，给提示
                stringBuffer.append("用户"+ userId +",权限" + permissionId +"删除权限失败;");
                tip = false;
            }
        }
        //分配完毕,检查布尔标记，是否全部成功
        if (tip == true){
            result = "删除权限操作成功！";
        }else {
            result = stringBuffer.toString();
        }
        return result;
    }




    //根据用户id查用户权限
    @Override
    public UserPermission selectUserPermissionByUserId(Integer userId) {
        return permissionDao.selectUserPermissionByUserId(userId);
    }

}
