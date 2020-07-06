package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    //分页查询
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);

        Page<Permission>page=permissionDao.selectByPermission(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

//    添加权限
    @Override
    public void add(Permission permission) {

          permissionDao.add(permission);
    }

//    删除权限
    @Override
    public void deleteById(Integer id) {
        //查询中间表是否有数据
        Long count = permissionDao.findPermissionAndRoleCountByPermissionId(id);
        if (count>0){
            throw new HealthException("当前权限在中间表中存在数据不能删除！");
        }
//        删除权限
        permissionDao.deleteById(id);
    }

//    通过id查询权限信息
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }
    //    修改权限
    @Override
    public void edit(Permission permission) {
        permissionDao.update(permission);
    }
}
