package com.changgou.goods.service.Impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired(required = false)
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        return brands;
    }
    private Example createExample(Brand brand){
        Example example = new Example(brand.getClass());

        if (brand!=null) {
            Example.Criteria criteria = example.createCriteria();

            if (! StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            if (! StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter",brand.getLetter());
            }
            if (! StringUtils.isEmpty(brand.getSeq())) {
                criteria.andEqualTo("seq",brand.getSeq());
            }
        }
        return example;
    }

    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);

        List<Brand> brands = brandMapper.selectAll();

        return new PageInfo<Brand>(brands);
    }

    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size, Brand brand) {
        PageHelper.startPage(page,size);
        Example example = createExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        return new PageInfo<Brand>(brands);
    }
}
