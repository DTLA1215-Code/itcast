package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Order;

import java.util.Map;

public interface OrderService {
    Order submit(Map<String, Object> orderInfo)throws HealthException;

    Map findById4Detail(Integer id);
}
