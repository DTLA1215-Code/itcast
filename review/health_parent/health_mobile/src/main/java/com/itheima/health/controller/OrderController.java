package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("submit")
    public Result submit(@RequestBody Map<String,Object>orderInfo){
//            1.从形参中取出手机号（转String）
        String telephone = (String)orderInfo.get("telephone");
//            2.RedisMessageConstant常量类调用静态变量SENDTYPE_ORDER(标识作用)，拼接手机号码，作为redis的key
        String redisKey = RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone;
//            3.创建redis对象
        Jedis redis = jedisPool.getResource();
//            4.jedis用key取出验证码value
        String redisValue = redis.get(redisKey);
//            5.判断是否为空
        if (redisValue == null) {
//            空则返回失败，提示用户点击发送验证码
            return new Result(false,"请点击发送验证码");
        }
//            6.判断redis验证码和提交的验证码是否一致
        if (!redisValue.equals((String) orderInfo.get("validateCode"))) {
//            不一样返回false，验证码不正确
            return new Result(false,"验证码不正确");
        }
//            7.redis删除验证码	API：del
        redis.del(redisKey);
//            8.orderInfo设置预约类型, Order.ORDERTYPE_WEIXIN（设置预约类型）
        orderInfo.put("orderType",Order.ORDERTYPE_WEIXIN);
//            9.提交至服务类submit，返回Order对象
        Order order = orderService.submit(orderInfo);
//            10.返回成功，MessageConstant.ORDER_SUCCESS,order
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    @PostMapping("findById")
    public Result findById(Integer id){
//        创建map对象
        Map map = null;
//        orderService调用findById4Detail方法，参数用id，结果赋值给map
        map = orderService.findById4Detail(id);
//        返回成功MessageConstant.QUERY_ORDER_SUCCESS
        if (map!=null) {
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }
//        返回失败，MessageConstant.QUERY_ORDER_FAIL
        return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
    }
}
