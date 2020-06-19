package com.itcast.dao;

import com.itcast.pojo.User;

import java.util.List;

public interface UserDao {

    public List<User> findAll();
}
