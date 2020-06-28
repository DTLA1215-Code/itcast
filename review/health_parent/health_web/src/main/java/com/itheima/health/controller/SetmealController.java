package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;
    //上传图片
    @PostMapping("upload")
    public Result upload(MultipartFile imgFile){
        String originalFilename = imgFile.getOriginalFilename();

        //获取后缀
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

        //生成唯一文件名
        String uniqueName = UUID.randomUUID().toString() + ext;

        Jedis jedis = jedisPool.getResource();
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),uniqueName);
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,uniqueName);
            System.out.println("上传图片："+uniqueName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }finally {
            if (jedis!=null)
                jedis.close();
        }

        Map<String,String>dataMap = new HashMap<>();
        dataMap.put("imgName",uniqueName);
        dataMap.put("domain",QiNiuUtils.DOMAIN);

        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,dataMap);
    }

    //添加套餐
    @PostMapping("add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        Jedis jedis = jedisPool.getResource();
        setmealService.add(setmeal,checkgroupIds);
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        System.out.println("添加套餐："+setmeal.getImg());
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal>pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    //修改图片
    @PostMapping("update")
    public Result update(@RequestBody Setmeal setmeal, Integer[]checkgroupIds){
        Jedis jedis = jedisPool.getResource();

        Setmeal oldSetmeal = setmealService.findById(setmeal.getId());

        setmealService.update(setmeal,checkgroupIds);

        jedis.srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,oldSetmeal.getImg());
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        jedis.close();

        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }


    @RequestMapping("findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        // 前端要显示图片需要全路径
        // setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        // setmeal通过上面的语句，img代表全路径=> formData绑定， img也是全路径 => 提交过来的setmeal.img全路径, 截取字符串 获取图片的名称
        // 封装到map中，解决图片路径问题
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("setmeal", setmeal); // formData
        resultMap.put("imageUrl", QiNiuUtils.DOMAIN + setmeal.getImg()); // imageUrl
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    @RequestMapping("findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
    }

    //删除套餐
    @RequestMapping("delete")
    public Result delete(int id){
        Jedis jedis = jedisPool.getResource();

        Setmeal setmeal = setmealService.findById(id);
        setmealService.delete(id);

        jedis.srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

        jedis.close();
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
