package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuDao menuDao;

    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        return user;
    }


    @Override
    public List<Map<String, Object>> getMenuList(String username) {
        User user = userDao.findUserByUsername(username);

        //根据 user.id 查一级菜单列表
        List<Menu> menuList = userDao.findMenuListByUserId(user.getId());

        List<Map<String,Object>> mapMenu = new ArrayList<>();

        for (Menu menu : menuList) {
            Map<String,Object> map = new HashMap<>();
            map.put("path",menu.getPath());
            map.put("title",menu.getName());
            map.put("icon",menu.getIcon());
            //查二级菜单
            List<Menu> menuChildrenList = userDao.findMenuListLevel2ByMenuId(menu.getId());

            List<Map<String,Object>> mapMenu1 = new ArrayList<>();
            if (menuChildrenList != null && menuChildrenList.size() > 0){
                for (Menu menu1 : menuChildrenList) {
                    Map<String,Object> map1 = new HashMap<>();
                    map1.put("path",menu1.getPath());
                    map1.put("title",menu1.getName());
                    map1.put("linkUrl",menu1.getLinkUrl());
                    mapMenu1.add(map1);
                }
            }
            map.put("children",mapMenu1);
            mapMenu.add(map);
        }
        return mapMenu;
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页的参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<CheckItem> page = userDao.findByCondition(queryString);
        // 3：封装PageResult数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userDao.findRoleIdsByUserId(id) ;
    }

    @Override
    public void add(User user, Integer[] roleIds) {
        //增加role
        userDao.add(user);

        //增加 role permission 关联
        this.setRoleAndPermission(user.getId(),roleIds);
    }

    @Override
    public void edit(User user, Integer[] roleIds) {
        //更新 user
        userDao.update(user);

        //删除 原来 user role 关联
        userDao.removeUserAndRole(user.getId());

        //增加 新 user role 关联
        this.setRoleAndPermission(user.getId(),roleIds);
    }

    @Override
    public void deleteById(Integer id) {
        //  先删除 role下的关联
        //删除 user role 关联
        userDao.removeUserAndRole(id);

        //  删除 user
        userDao.deleteById(id);
    }

    private void setRoleAndPermission(Integer userId, Integer[] roleIds) {
        if(roleIds!=null && roleIds.length>0){
            for (Integer roleId : roleIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("userId",userId);
                map.put("roleId",roleId);
                userDao.setRoleAndPermission(map);
            }
        }
    }

    /**
     * @description: 根据用户名查询菜单
     * @author: QIXIANG LING
     * @date: 2020/7/8 10:00
     * @param: username
     * @return: java.util.List<com.itheima.health.pojo.Role>
     *     获取用户所拥有的菜单列表  menuList
     *     用户所拥有的一级菜单列表 menuFirst
     */
    @Override
    public List<Menu> getMenu(String username) {
        // 查询用户所拥有的角色列表
        List<Role> roleList = userDao.findRole(username);
        List<Menu> menuList = new ArrayList<>();
        // 遍历获取每个用户所拥有的角色, 在查出每个角色所拥有的菜单列表,再遍历出来存到列表中
        for (Role role : roleList) {
            List<Menu> list = menuDao.findMenu(role);
            // 获取用户所拥有的菜单列表
            for (Menu menu : list) {
                menuList.add(menu);
            }
        }
        // menuList 就是用户所拥有的菜单; 查出一级菜单 menuFirst
        List<Menu> menuFirst = new ArrayList<>();
        for (Menu menu : menuList) {
            // 如果没有parentMenuid; 就查询它的子菜单
            if(null == menu.getParentMenuId()){
                List<Menu> childrenMenu = menuDao.findByParentId(menu.getId());
                menu.setChildren(childrenMenu);
                menuFirst.add(menu);
            }
        }
        return menuFirst;

    }
}
