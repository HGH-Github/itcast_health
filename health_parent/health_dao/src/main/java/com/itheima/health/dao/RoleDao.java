package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleDao {
    //查询总数
    Long findSum();

    //分页查询
    List<Role> findPage(Map<String, Object> map);

    List<Role> findPage2(@Param ( "index" ) Integer index, @Param ( "pageSize" )Integer pageSize,@Param ( "queryString" ) String queryString);


    Page<Role> findByCondition(String queryString);

    List<Menu> findAllWithMenu();

    List<Permission> findAllWithPermission();

    void addRole(Role role);

    void addRoleWithMenu(@Param ( "roleId" ) Integer roleId,@Param ( "menuId" ) Integer menuId);

    void addRoleWithPermission(@Param ( "roleId" )Integer roleId, @Param ( "permissionId" )Integer permissionId);

    List<Integer> findMenuIds(Integer roleId);

    List<Integer> findPermissionIds(Integer roleId);

    Role findById(Integer roleId);

    void deleteRoleWithMenuById(Integer roleId);

    void deleteRoleWithPermissionById(Integer roleId);


    void updateRole(Map<String, Object> roleMap);

    Integer findUserWithRole(Integer id);

    void deleteRole(Integer id);
}
