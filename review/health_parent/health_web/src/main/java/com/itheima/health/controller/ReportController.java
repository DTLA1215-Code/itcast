package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.SetmealService;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("report")
@RestController
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @RequestMapping("getMemberReport")
    public Result getMemberReport(){
        List<String> months= new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH,-12);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,+1);
            Date date = calendar.getTime();
            String month = dateFormat.format(date);
            months.add(month);
        }

        Map<String,Object>map = new HashMap<>();
        List<Integer>memberCount = memberService.findMembersByMonths(months);
        map.put("months",months);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @RequestMapping("getSetmealReport")
    public Result getSetmealReport(){
        List<Map<String,Object>>setmealCount = setmealService.findSetmealCount();

        List<String>setmealName = new ArrayList<>();

        if (setmealCount!=null) {
            for (Map<String, Object> setmeal : setmealCount) {
                setmealName.add((String)setmeal.get("name"));
            }
        }

        Map<String,Object>resultMap = new HashMap<>();

        resultMap.put("setmealCount",setmealCount);
        resultMap.put("setmealName",setmealName);

        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    @Test
    public void test(){
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH,-12);

        System.out.println(calendar.getTime());
    }
}
