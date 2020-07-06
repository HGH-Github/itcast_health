package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Permission;

public interface PermissionService  {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void add(Permission permission);

    void deleteById(Integer id) throws HealthException;

    Permission findById(Integer id);

    void edit(Permission permission);
}
