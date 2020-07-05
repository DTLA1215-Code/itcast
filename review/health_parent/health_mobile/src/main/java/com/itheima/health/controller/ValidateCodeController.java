package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("send4Order")
    public Result send4Order(String telephone){
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        //RedisMessageConstant常量类调用静态变量SENDTYPE_ORDER(标识作用)，拼接手机号码，作为redis的key
        String redisKey = RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone;
        //jedis通过key获取value验证码
        String redisValue = jedis.get(redisKey);
        //判断验证码是否为空
        if (redisValue==null) {
            try {
                //不存在，则调用工具类生成六位数验证码
                Integer code = ValidateCodeUtils.generateValidateCode(6);
                //用工具类发送验证码（SMSUtils工具类参量，电话，验证码{需要int转String}）
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
                //存入redis，参数需要key、有效时间、验证码{需要int转String}
                jedis.setex(redisKey,60*15,code+"");
                //return成功		MessageConstant.SEND_VALIDATECODE_SUCCESS
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                //catch	return失败		MessageConstant.SEND_VALIDATECODE_FAIL
                e.printStackTrace();
                return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //存在，return成功		MessageConstant.SENT_VALIDATECODE
        return new Result(true,MessageConstant.SENT_VALIDATECODE);
    }

    @PostMapping("send4Login")
    public Result send4Login(String telephone){
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        //RedisMessageConstant常量类调用静态变量SENDTYPE_ORDER(标识作用)，拼接手机号码，作为redis的key
        String redisKey = RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone;
        //jedis通过key获取value验证码
        String redisValue = jedis.get(redisKey);
        //判断验证码是否为空
        if (redisValue==null) {
            try {
                //不存在，则调用工具类生成六位数验证码
                Integer code = ValidateCodeUtils.generateValidateCode(6);
                //用工具类发送验证码（模板号，电话，验证码{需要int转String}）
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
                //存入redis，参数需要key、有效时间、验证码{需要int转String}
                jedis.setex(redisKey,60*15,code+"");
                //return成功		MessageConstant.SEND_VALIDATECODE_SUCCESS
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                //catch	return失败		MessageConstant.SEND_VALIDATECODE_FAIL
                e.printStackTrace();
                return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //存在，return成功		MessageConstant.SENT_VALIDATECODE
        return new Result(true,MessageConstant.SENT_VALIDATECODE);
    }
}
