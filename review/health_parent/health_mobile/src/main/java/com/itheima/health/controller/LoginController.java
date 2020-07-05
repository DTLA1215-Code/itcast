package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    //    check方法，参数HttpServletResponse + map
    @PostMapping("check")
    public Result check(HttpServletResponse response, @RequestBody Map<String, Object>loginInfo){
        //    1.map集合取出手机号
        String telephone = (String)loginInfo.get("telephone");
        //    2.map集合取出验证码
        String code = (String)loginInfo.get("validateCode");
        //    3.创建redis对象
        Jedis redis = jedisPool.getResource();
        //    拼接redis的key对象 RedisMessageConstant.SENDTYPE_LOGIN+“_”+手机号
        String redisKey = RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone;
        //    4.redis取出验证码
        String redisValue = redis.get(redisKey);
        //    5.判断验证码是否为空以及是否一致
        if (redisValue==null|| ! redisValue.equals(code)) {
            //            不一致返回false
            return new Result(false,"验证码不正确");
        }
        //成功的话，则清除验证码
        redis.del(redisKey);
        //判断是否是会员
        //		1.memberService通过手机号findByTelephone获取会员对象
        Member member = memberService.findByTelephone(telephone);
        //            判断会员是否为空
        if (member==null) {
            //			1.空，则创建会员
            member = new Member();
            //			2.添加手机号
            member.setPhoneNumber(telephone);
            //			3.添加创建日期
            member.setRegTime(new Date());
            //            添加备注
            member.setRemark("手机快速登录");
            //			4.memberService往表中添加会员add
            memberService.add(member);
        }
        //    //跟踪记录的手机号码，代表着会员
        //    创建cookie对象，参数写(“login_member_telephone”，手机号)
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        //    设置存活时间setMaxAge
        cookie.setMaxAge(60*60*24*30);
        //    设置访问的路径，用/反斜线
        cookie.setPath("/");
        //    response添加cookie
        response.addCookie(cookie);
        //    返回成功,MessageConstant.LOGIN_SUCCESS
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

}
