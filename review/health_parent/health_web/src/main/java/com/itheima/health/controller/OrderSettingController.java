package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping("upload")
    public Result upload(MultipartFile excelFile){
        try {
            List<String[]> excel = POIUtils.readExcel(excelFile);

            List<OrderSetting>orderSettingList = new ArrayList<>();


            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);

            Date orderDate = null;
            OrderSetting os = null;

            for (String[] dataArr : excel) {
                orderDate = sdf.parse(dataArr[0]);

                int number = Integer.valueOf(dataArr[1]);

                os = new OrderSetting(orderDate,number);

                orderSettingList.add(os);
            }

            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        List<Map<String,Integer>> monthDate = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true ,MessageConstant.GET_ORDERSETTING_SUCCESS,monthDate);
    }

    @PostMapping("editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
