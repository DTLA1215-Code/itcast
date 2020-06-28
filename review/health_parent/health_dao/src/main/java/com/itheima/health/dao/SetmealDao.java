package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId,@Param("checkgroupId") Integer checkgroupId);

    Page<Setmeal> findByCondition(String queryString);

    void update(Setmeal setmeal);

    void deleteSetmealCheckGroup(Integer id);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    Setmeal findById(int id);

    int findOrderCountBySetmealId(int id);

    void deleteById(int id);

    List<Setmeal> findAll();

    Setmeal findDetailById(int id);
}
