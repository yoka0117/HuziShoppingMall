package com.huzi.consumer.controller.permission;
import com.huzi.domain.permission.Permission;
import com.huzi.service.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


//权限设置
@Controller
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    //添加权限的设置
    @RequestMapping("addPermission")
    public ModelAndView addPermission(Permission permission){
        ModelAndView mv = new ModelAndView();
        String result = permissionService.addPermission(permission);
        mv.addObject("result",result);
        mv.setViewName("permission/addPermission");
        return mv;
    }


    //废弃权限的设置
    @RequestMapping("discardPermission")
    public ModelAndView discardPermission(Integer permissionId){
        ModelAndView mv = new ModelAndView();
        String result = "";
        if (permissionService.discardPermission(permissionId)>0){
            result = "权限" + permissionId +"废弃成功";
        }

        mv.addObject("result",result);
        mv.setViewName("permission/discardPermission");
        return mv;
    }



    //给用户设置权限
    @RequestMapping("setPermissionsForUser")
    public ModelAndView setPermissionsForUser(String userId,String permissionId){
        ModelAndView mv = new ModelAndView();
        String result = "";
        //先判断是否有空值
        if (userId == null || permissionId == null ){
            result = "输入有空值，请重新输入";
        }else {
            result = permissionService.setPermissionsForUser(userId, permissionId);
        }
        mv.addObject("result" , result);
        mv.setViewName("permission/setPermissionsForUser");
        return mv;
    }




    //消除用户权限(与"添加权限"逻辑类似，只有mapper文件不同)
    @RequestMapping("deletePermissionsForUser")
    public ModelAndView deletePermissionsForUser(String userId,String permissionId){
        ModelAndView mv = new ModelAndView();
        String result = "";
        if (userId == null || permissionId == null ){
            result = "输入有空值，请重新输入";
        }else {
            result = permissionService.deletePermissionsForUser(userId, permissionId);
        }
        mv.addObject("result" , result);
        mv.setViewName("permission/deletePermissionsForUser");
        return mv;
    }



}
