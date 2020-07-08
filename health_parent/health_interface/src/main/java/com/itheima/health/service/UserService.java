package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findUserByUsername(String username);

    List<Map<String, Object>> getMenuList(String username);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void add(User user, Integer[] permissionIds);

    void edit(User user, Integer[] roleIds);

    void deleteById(Integer id);
}
