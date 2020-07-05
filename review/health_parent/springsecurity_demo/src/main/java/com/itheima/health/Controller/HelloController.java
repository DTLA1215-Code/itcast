package com.itheima.health.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("hello")
public class HelloController {

    @RequestMapping("add")
    @PreAuthorize("hasAuthority('ADD_CHECKITEM')")
    public String add(){
        System.out.println("add方法");
        return "/index.html";
    }

    @RequestMapping("delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(){
        System.out.println("delete方法");
        return "hello".trim();
    }
}
