package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User findUserByUsername(String username);

    List<Menu> findMenuListByUserId(Integer id);

    List<Menu> findMenuListLevel2ByMenuId(Integer id);

    Page<CheckItem> findByCondition(String queryString);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void add(User user);

    void setRoleAndPermission(Map<String, Object> map);

    void update(User user);

    void removeUserAndRole(Integer id);

    void deleteById(Integer id);
}
