package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    //分页查询
    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
//        Integer currentPage = queryPageBean.getCurrentPage ();//当前页码
//        String queryString = queryPageBean.getQueryString (); //查询条件
//        Integer pageSize = queryPageBean.getPageSize (); //每页记录数
//        Integer  index = (currentPage-1)*pageSize; //开始索引
//         Map<String,Object> map = new HashMap<> (  );
//         map.put ( "index",index );
//        map.put ( "pageSize",pageSize );
//        map.put ( "queryString",queryString );
//        List<Role> rows = roleDao.findPage(map);
////      List<Role> rows2 = roleDao.findPage2(index,pageSize,queryString);
//        Long total = roleDao.findSum();
//        return new PageResult<Role> (total,rows);
        // 使用PageHelper.startPage
        PageHelper.startPage ( queryPageBean.getCurrentPage (),queryPageBean.getPageSize () );
        //有查询条件的话 模糊查询
        if (!StringUtils.isEmpty ( queryPageBean.getQueryString () )){
            // 拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString()+ "%");
        }
// 紧接着的查询会被分页
        Page<Role> page  = roleDao.findByCondition(queryPageBean.getQueryString());
                return new PageResult<Role> (page.getTotal (),page.getResult ());
    }

    @Override
    public Map<String, Object> findAllWithMenuAndPermission() {
        List<Menu> menuList = roleDao.findAllWithMenu2();
        List<Permission> permissionList = roleDao.findAllWithPermission();
        Map<String,Object> map = new HashMap<> (  );
        map.put ( "menuList",menuList );
        map.put ( "permissionList",permissionList );
        return map;
    }

    @Override
    @Transactional
    public void add(Integer[] menuIds, Integer[] permissionIds, Role role) {
        //  先添加 role 返回id
        roleDao.addRole(role);
        Integer roleId = role.getId ();
        if (null!=menuIds) {
            //在添加联合表
            for (int i = 0; i < menuIds.length; i++) {
                roleDao.addRoleWithMenu ( roleId, menuIds[i] );
            }
        }
        if (null!=permissionIds) {
            for (int i = 0; i < permissionIds.length; i++) {
                roleDao.addRoleWithPermission ( roleId, permissionIds[i] );
            }
        }
    }

    //回显数据
    @Override
    public Map<String, Object> findById(Integer roleId) {
        //根据id 查询联合表 返回list集合
        List<Integer> menuIds = roleDao.findMenuIds(roleId);
        List<Integer> permissionIds = roleDao.findPermissionIds(roleId);
        //查询role表
        Role roleBasic = roleDao.findById(roleId);
        Map<String,Object> map=new HashMap<String, Object> ( );
        map.put ( "menuIds",menuIds );
        map.put ( "permissionIds",permissionIds );
        map.put ( "roleBasic",roleBasic );
        return map;
    }

    //修改角色
    @Override
    @Transactional
    public void update(Map<String, Object> map) {
        //接受转化参数
        List<Integer> menuIds =  (List<Integer>)map.get ( "menuIds" );
        List<Integer> permissionIds =  (List<Integer>)map.get ( "permissionIds" );
      Map<String,Object> roleMap = (Map<String, Object>) map.get ( "roleBasic" );


//        Role role = new Role ();
//        System.out.println (role);
//        try {
//            BeanUtils.populate ( role,(Map<String,Object>) map.get ( "roleBasic" ) );
//        } catch (Exception e) {
//            e.printStackTrace ();
//        }
//        添加数据
//        先根据删除联合表 在添加联合表
        Integer roleId = (Integer) roleMap.get ( "id" );
        roleDao.deleteRoleWithMenuById(roleId);
        if (menuIds!=null){
        for (Integer menuId : menuIds) {
            roleDao.addRoleWithMenu(roleId,menuId);
        }}
        roleDao.deleteRoleWithPermissionById(roleId);
        if (permissionIds!=null){
        for (Integer permissionId : permissionIds) {
            roleDao.addRoleWithPermission(roleId,permissionId);
        }}

       roleDao.updateRole(roleMap);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws HealthException {
        //先查询用联合表有没有关联
        Integer count = roleDao.findUserWithRole(id);
        //有的话抛异常
          if (count>0){
              throw new HealthException ( "该角色和用户关联，不能删除！" );
          }
        //没有的话 把角色表  角色和菜单  角色和权限 联合表删除
          roleDao.deleteRoleWithMenuById ( id);
          roleDao.deleteRoleWithPermissionById ( id );
          roleDao.deleteRole(id);
    }

    @Override
    public List<Role> findAll() {
        List<Role> roleList=roleDao.findAll();
        return roleList;
    }
}
