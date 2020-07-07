package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    PageResult<Role> findPage(QueryPageBean queryPageBean);

    Map<String,Object> findAllWithMenuAndPermission();

    void add(Integer[] menuIds, Integer[] permissionIds, Role role);

    Map<String, Object> findById(Integer roleId);

    void update(Map<String, Object> map);

    void deleteById(Integer id) throws HealthException;
}
