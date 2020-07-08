package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Reference
    UserService userService;

    // 从SpringSecurity中获取用户信息，显示username对应的模型中
    @RequestMapping(value = "/getUsername")
    public Result getUsername(){
        try {
            // 使用SpringSecurity的方式完成
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();
            // 使用登录名，查询用户信息，返回用户对象
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }

    @RequestMapping("/getMenuList")
    public Result getMenuList()throws Exception{
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //通过user.name 查 具体的菜单列表
            // t_user -> t_user_role -> t_role_menu -> t_menu
            List<Map<String,Object>> list = userService.getMenuList(user.getUsername());
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,list);
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    // 分页查询用户列表
    @RequestMapping(value = "/findPage")
    //@PreAuthorize(value = "hasAuthority('USER_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = userService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        com.itheima.health.pojo.User user = userService.findById(id);
        if(user!=null){
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS,user);
        }
        else{
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

    @RequestMapping(value = "/findRoleIdsByCheckGroupId")
    public List<Integer> findRoleIdsByCheckGroupId(Integer id){
        List<Integer> list  = userService.findRoleIdsByUserId(id);
        return list;
    }

    @RequestMapping(value = "/add")
    @PreAuthorize(value = "hasAuthority('USER_ADD')")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer [] roleIds){
        try {
            userService.add(user,roleIds);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_USER_FAIL);
        }
    }

    @RequestMapping(value = "/edit")
    @PreAuthorize(value = "hasAuthority('USER_EDIT')")
    public Result edit(@RequestBody com.itheima.health.pojo.User user, Integer [] roleIds){
        try {
            userService.edit(user,roleIds);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
    }

    // 删除用户
    @RequestMapping(value = "/deleteById")
    @PreAuthorize(value = "hasAuthority('USER_DELETE')")
    public Result deleteById(Integer id){
        try {
            userService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
        } catch(RuntimeException e){
            return new Result(false,e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }
    }

}
