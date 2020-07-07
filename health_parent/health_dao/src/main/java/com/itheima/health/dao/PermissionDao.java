package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;

public interface PermissionDao {
//    分页查询
    Page<Permission> selectByPermission(String queryString);
//    添加权限
    void add(Permission permission);
//    查询中间表是否有数据
    Long findPermissionAndRoleCountByPermissionId(Integer id);
//    删除权限
    void deleteById(Integer id);
    //    通过id查询权限信息
    Permission findById(Integer id);
    //    修改权限
    void update(Permission permission);
}
