package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Order submit(Map<String, Object> orderInfo) {
//        1.orderInfo取出日期
        String orderDate = (String)orderInfo.get("orderDate");
//        2.创建Date日期变量
        Date date = new Date();
//        3.取出的日期按照（yyyy-MM-dd）格式转换后付给Date
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(orderDate);
        } catch (ParseException e) {
//        4.捕获异常，友好回复
            throw new HealthException("您输入的日期格式不正确");
        }
//        5.orderSettingDao调用方法findByOrderDate通过日期变量，获取OrderSetting对象（预约日期、可预约人数、已预约人数）
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
//        6.判断是否为空，空则友好提示
        if (orderSetting==null) {
            throw new HealthException("您选中的日期还不可预约");
        }
//        7.判断可预约和已预约，大于则友好提示MessageConstant.ORDER_FULL
        if (orderSetting.getReservations()>=orderSetting.getNumber()) {
            throw new HealthException(MessageConstant.ORDER_FULL);
        }
//        8.未满 可预约 update reservations+1	orderSettingDao调用方法editReservationsByOrderDate，传递参数orderSetting
        orderSettingDao.editReservationsByOrderDate(orderSetting);
//                ------------------------------
//        1.orderInfo取出手机号（转String）
        String telephone = (String)orderInfo.get("telephone");
//        2.memberDao调用方法findByTelephone，获取会员对象
        Member member = memberDao.findByTelephone(telephone);
//        3.new一个Order
        Order order = new Order();
//        4.orderInfo获取套餐id，付给Order
        order.setSetmealId(Integer.parseInt((String)orderInfo.get("setmealId")));
//        5.预约日期赋给Order
        order.setOrderDate(date);
//        6.判断是否有会员
        if (member==null) {
//        1.空，则new一个会员
            member = new Member();
//        2.设置注册日期
            member.setRegTime(new Date());
//        3.设置性别，orderInfo取出
            member.setSex((String) orderInfo.get("sex"));
//        4.设置手机号
            member.setPhoneNumber(telephone);
//        5.设置身份证
            member.setIdCard((String)orderInfo.get("validateCode"));
//        6.设置姓名
            member.setName((String)orderInfo.get("name"));
//        7.memberDao.add(member)添加会员
            memberDao.add(member);
        }
//        1.非空，则member取出id赋给order
        order.setMemberId(member.getId());
//        2.orderDao调用方法findByCondition，传递参数order，获取List<Order> orderList
        List<Order> condition = orderDao.findByCondition(order);
//        3.判断是否重复预约：不等于null或size大于0，则抛出友好提示MessageConstant.HAS_ORDERED
        if (condition!=null&&condition.size()>0) {
            throw new HealthException(MessageConstant.HAS_ORDERED);
        }
//                --------------------------------
//        1.order设置预约类型
        order.setOrderType((String) orderInfo.get("orderType"));
//        2.order设置预约状态
        order.setOrderStatus(order.ORDERSTATUS_NO);
//        3.orderDao调用方法add(order);
        orderDao.add(order);
//        4.返回order
        return order;
    }

    @Override
    public Map findById4Detail(Integer id) {
        Map map = null;
//        orderDao调用方法findById4Detail，参数用id，结果赋值给map
        map = orderDao.findById4Detail(id);
//        判断map，不为空则map取出orderDate
        if (map!=null) {
            Date date = (Date) map.get("orderDate");
//        map重新put orderDate用工具类转成String类型
            try {
                map.put("orderDate", DateUtils.parseDate2String(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }
//        返回map
        return map;
    }
}
