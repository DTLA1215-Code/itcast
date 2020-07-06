package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.SetmealService;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
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

    @RequestMapping("getMemberReportBetweenDays")
    public Result getMemberReportBetweenDays(String start,String end){
        List<String>months = new ArrayList<>();

        //2020-01-01 ---->  2020-04-01
        String[] splitStart = start.split("-");
        String[] splitEnd = end.split("-");
        int diffDate = Integer.valueOf(splitEnd[1]) - Integer.valueOf(splitStart[1]);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = simpleDateFormat.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);

        String newStart = start.substring(0, 7);
        months.add(newStart);
        for (int i = 0; i < diffDate; i++) {
            calendar.add(Calendar.MONTH,+1);
            Date dateMonth = calendar.getTime();
            String everyMonth = simpleDateFormat.format(dateMonth);
            months.add(everyMonth);
        }
        String newEnd = end.substring(0, 7);
        months.add(newEnd);

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
        String date1 = "2012-06-01";
        String date2 = "2012-12-01";

        System.out.println(date1.substring(0,7));
        /*        String[] splitStart = date1.split("-");
        String[] splitEnd = date2.split("-");
        int diffDate = Integer.valueOf(splitEnd[1]) - Integer.valueOf(splitStart[1]);
//        System.out.println(diffDate);
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

        Date date = null;

        try {
            date = simpleDateFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);
        Date dateMonth = calendar.getTime();
        String everyMonth = simpleDateFormat.format(dateMonth);
        System.out.println(everyMonth);*/
    }
}
