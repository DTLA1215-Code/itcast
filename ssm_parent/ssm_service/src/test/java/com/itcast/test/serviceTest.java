package com.itcast.test;

import com.itcast.pojo.User;
import com.itcast.service.UserService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class serviceTest {

/*    @Autowired
    private UserService userService;*/

    @Test
    public void test_1()throws Exception{
/*        for (User user : userService.findAll()) {
            System.out.println(user);
        }*/

        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("spring_service.xml");
        UserService userService = app.getBean(UserService.class);
        for (User user : userService.findAll()) {
            System.out.println(user);
        }
    }
}
