package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    Setmeal findById(int id);

    void delete(int id)throws HealthException;

    List<Setmeal> findAll();

    Setmeal findDetailById(int id);

    List<Map<String, Object>> findSetmealCount();

}
