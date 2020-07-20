package com.changgou.goods.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @RequestMapping
    public Result<List<Brand>> findAll(){
        List<Brand> list = brandService.findAll();
        return new Result<>(true, StatusCode.OK,"查询成功",list);
    }

    @GetMapping("/{id}")
    public Result<Brand>findById(@PathVariable(value = "id") Integer id){
        Brand brand = brandService.findById(id);
        return new Result<>(true,StatusCode.OK,"查询成功",brand);
    }

    @PostMapping
    public Result add(Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"保存成功");
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(value = "id") Integer id,@RequestBody Brand brand){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value = "id") Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    public Result<List<Brand>> findList(@RequestBody(required = false) Brand brand){
        List<Brand> list = brandService.findList(brand);
        return new Result<List<Brand>>(true,StatusCode.OK,"查询成功",list);
    }

    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> search(@PathVariable(value = "page") Integer page, @PathVariable("size") Integer size){
        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Brand brand,@PathVariable(value = "page")Integer page,@PathVariable(value = "size")Integer size){
        PageInfo<Brand> pageInfo = brandService.findPage(page, size, brand);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }
}
