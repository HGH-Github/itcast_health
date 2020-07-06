package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 查询所有
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        //先从redis中查询，是否有套餐列表数据
        Jedis jedis = jedisPool.getResource();

        List<Setmeal> list=null;

        if (jedis.exists("setmealList")){
            try {
                //redis中存在，取出值，使用ObjectMapper读取出来
                String json=jedis.get("setmealList");

                list=objectMapper.readValue(json,List.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            // 从数据库查询所有的套餐
            list = setmealService.findAll();
            // 套餐里有图片有全路径吗? 拼接全路径
            list.forEach(s -> {
                s.setImg(QiNiuUtils.DOMAIN + s.getImg());
            });
            String json = null;

            try {
                //查询完之后，将查询到数据存入redis中
                json = objectMapper.writeValueAsString(list);
                //设置存活时间
                jedis.setex("setmealList",30*24*60*60,json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        //返回结果
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findById(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        Jedis jedis = jedisPool.getResource();

        Setmeal setmeal=null;

        if (jedis.exists("setmealDetail"+id)){
            //取值
            String json = jedis.get("setmealDetail" + id);

            //读取
            try {
                setmeal=objectMapper.readValue(json,Setmeal.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            // 调用服务查询详情
            setmeal = setmealService.findDetailById(id);
            // 设置图片的完整路径
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());

            //转换成字符串
            String json = null;
            try {
                json = objectMapper.writeValueAsString(setmeal);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            //存入redis
            jedis.setex("setmealDetail"+id,30*24*60*60,json);
        }
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findDetailById2")
    public Result findDetailById2(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findDetailById2(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findDetailById3")
    public Result findDetailById3(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findDetailById3(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
