package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    //分页查询
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = permissionService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()

        );
        return pageResult;

    }
    //新增权限
    @PostMapping("/add")
    private Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.ADD_PERMISSION_SUCCESS);
    }

//    删除权限
    @GetMapping("/delete")
    private Result delete(Integer id){
        try {
            permissionService.deleteById(id);
        }  catch (HealthException e) {
            return new Result(false, e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }
        return  new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
    }
//    通过id查询权限
    @GetMapping("/findById")
    private Result findById(Integer id){
        Permission permission = null;
        try {
            permission = permissionService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_PERMISSION_SUCCESS);
        }
        return new Result(true,MessageConstant.GET_PERMISSION_FAIL,permission);
    }
//    修改权限
    @PostMapping("/edit")
    private Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
    }
 }
