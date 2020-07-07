package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import javafx.beans.binding.ObjectExpression;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    //分页查询
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Role> pageResult = roleService.findPage(queryPageBean);
        return new Result ( true, "查询角色成功",pageResult );
    }
    //查询菜单和权限列表
    @RequestMapping("/findAllWithMenuAndPermission")
    public Result findAllWithMenuAndPermission(){
       Map<String,Object> map = roleService.findAllWithMenuAndPermission();
        return new Result ( true,"初始化菜单和权限列表成功",map );
    }
   //添加
    @RequestMapping("/add")
    public Result add(@RequestBody Map<String,Object> map ){
        List<Integer> menuIdsList =   (List<Integer>) map.get ( "menuIds" )  ;
        Integer[] menuIds = (Integer[])menuIdsList.toArray (new Integer[menuIdsList.size ()]);
        List<Integer> permissionIdsList =  (List<Integer>) map.get ( "permissionIds" );
        Integer[] permissionIds = (Integer[])permissionIdsList.toArray (new Integer[permissionIdsList.size ()]);
        System.out.println (map.get ( "roleBasic" ));
        Role role = new Role ();
        System.out.println (role);
        try {
            BeanUtils.populate (role ,(Map<String,Object>) map.get ( "roleBasic" ) );
            roleService.add(menuIds,permissionIds,role);
        } catch (Exception e) {
            e.printStackTrace ();
            return new Result ( false,"添加角色失败" );
        }
        return new Result ( true,"添加角色成功" );
    }
    //编辑回显
    @RequestMapping("/findById")
    public Result findById(Integer roleId){
        Map<String,Object> map = roleService.findById(roleId);
        return new Result ( true,"获取角色信息成功",map );
    }
    //编辑
    @RequestMapping("/update")
    public Result update(@RequestBody Map<String,Object> map){
        //调用service修改
        try {
            roleService.update(map);
        } catch (Exception e) {
            e.printStackTrace ();
            return new Result ( false,"修改角色失败" );
        }
        return new Result ( true,"修改角色成功" );
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
            roleService.deleteById(id);
            return new Result ( true,"角色删除成功" );

    }
}
