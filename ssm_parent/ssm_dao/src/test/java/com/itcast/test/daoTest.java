package com.itcast.test;

import com.itcast.dao.UserDao;
import com.itcast.pojo.User;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class daoTest {

/*    @Autowired
    private UserDao userDao;*/

    @Test
    public void test_1()throws Exception{
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("ssm_Mybatis.xml");
        UserDao userDao = app.getBean(UserDao.class);
        for (User user : userDao.findAll()) {
            System.out.println(user);
        }

/*        for (User user : userDao.findAll()) {
            System.out.println(user);
        }*/
    }
}
