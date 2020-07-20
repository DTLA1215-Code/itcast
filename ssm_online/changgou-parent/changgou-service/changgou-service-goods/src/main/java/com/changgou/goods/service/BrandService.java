package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {

    public List<Brand>findAll();

    public Brand findById(Integer id);

    public void add(Brand brand);

    public void update(Brand brand);

    public void delete(Integer id);

    List<Brand>findList(Brand brand);

    public PageInfo<Brand> findPage(Integer page, Integer size);

    PageInfo<Brand> findPage(Integer page,Integer size,Brand brand);
}
