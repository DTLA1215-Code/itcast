package com.itheima.health.security;

import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名获取用户
        com.itheima.health.pojo.User userInDB = findByUsername(username);
        //创建权限集合
        List<GrantedAuthority>authorities = new ArrayList<>();
        //通过用户获取角色
        Set<Role>roles =  userInDB.getRoles();

        if (roles!=null) {
            for (Role role : roles) {
                //获取角色名
                GrantedAuthority ga = new SimpleGrantedAuthority(role.getName());
                authorities.add(ga);

                for (Permission permission : role.getPermissions()) {
                    //获取权限名
                    ga = new SimpleGrantedAuthority(permission.getName());
                    authorities.add(ga);
                }
            }
        }
        //生成用户
        User user = new User(username,userInDB.getPassword(),authorities);

        return user;
    }

    private com.itheima.health.pojo.User findByUsername(String username){
        if (username.equals("admin")) {
            com.itheima.health.pojo.User user = new com.itheima.health.pojo.User();
            user.setUsername("admin");
            user.setPassword("$2a$10$MBw2qGqUSYj2.T5eHIf78eykwRe0cqcQBtLKFNKAzprICw5NDF/Y6");

            Permission permission = new Permission();
            permission.setName("ADD_CHECKITEM");

            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

            Role role = new Role();
            role.setName("ROLE_ADMIN");
            role.setPermissions(permissions);

            Set<Role> roles = new HashSet<>();
            roles.add(role);

            user.setRoles(roles);

            return user;
        }
        return null;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("admin"));

        //System.out.println(encoder.matches("admin","$2a$10$MBw2qGqUSYj2.T5eHIf78eykwRe0cqcQBtLKFNKAzprICw5NDF/Y6"));
    }
}
