package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) {

        for (OrderSetting orderSetting : orderSettingList) {

            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

            if (osInDB!=null) {
                if (osInDB.getReservations() > orderSetting.getNumber()) {
                    throw new HealthException(orderSetting.getOrderDate() + " 中已预约数量不能大于可预约数量");
                }
                orderSettingDao.updateNumber(orderSetting);
            }else {
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
//        2020-06--------------
//        2020-06-01 2020-06-31--------------
        String startDate = month + "-01";
        String endDate = month + "-31";
        return orderSettingDao.getOrderSettingBetweenDate(startDate,endDate);
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

        if (osInDB!=null) {
            if (osInDB.getReservations() > orderSetting.getNumber()) {
                throw new HealthException(orderSetting.getOrderDate() + " 中已预约数量不能大于可预约数量");
            }
            orderSettingDao.updateNumber(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}
