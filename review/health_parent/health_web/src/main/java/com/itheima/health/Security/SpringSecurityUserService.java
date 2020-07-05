package com.itheima.health.Security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        //创建‘GrantedAuthority 已授予权限’的权限集
        List<GrantedAuthority>authorities = new ArrayList<>();

        GrantedAuthority sga = null;

        Set<Role> roles = user.getRoles();
        if(roles!=null){
            for (Role role : roles) {
                sga = new SimpleGrantedAuthority(role.getKeyword());
                authorities.add(sga);
                Set<Permission> permissions = role.getPermissions();
                for (Permission permission : permissions) {
                    sga = new SimpleGrantedAuthority(permission.getKeyword());
                    authorities.add(sga);
                }
            }
            return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
        }

        return null;
    }
}
