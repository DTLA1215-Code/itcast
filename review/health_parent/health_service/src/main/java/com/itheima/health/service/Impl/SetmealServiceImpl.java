package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 存放生成静态化页面的目录，开发时，存到health_mobile/webapp/pages下，
     * 上线时，要存放到health_mobile的tomcat目录下
     */
    @Value("${out_put_path}")
    private String out_put_path;

    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.先添加套餐
        setmealDao.add(setmeal);

        Integer setmealId = setmeal.getId();

        //2.添加套餐和检查组的关系（多对多）
        if (checkgroupIds!=null) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId,checkgroupId);
            }
        }

        //新增套餐后，需要生成静态页面
//        generateMobileStaticHtml();
    }

    //静态套餐详情和静态套餐列表分支
    private void generateMobileStaticHtml(){
        try {
            // ①套餐列表静态页面
            generateSetmealListHtml();

            // ②套餐详情静态页面 生成单就行了，为了测试方便，生成所有的
            generateSetmealDetailHtml();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //①套餐列表模板
    private void generateSetmealListHtml()throws Exception {
        //读取模板，获取template模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal.ftl");

        List<Setmeal> setmealList = setmealDao.findAll();

        for (Setmeal setmeal : setmealList) {
            setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        }

        Map<String,Object>dataMap = new HashMap<>();
        dataMap.put("setmealList",setmealList);

        template.process(dataMap,new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(out_put_path,"m_setmeal.html")),"utf-8")));
    }

    //②分两步，一先获取套餐详情，二再用详情生成静态
    private void generateSetmealDetailHtml() throws Exception {
        List<Setmeal> setmealList = setmealDao.findAll();

        for (Setmeal setmeal : setmealList) {
            Setmeal setmealDetail = setmealDao.findDetailById(setmeal.getId());

            setmealDetail.setImg(QiNiuUtils.DOMAIN+setmealDetail.getImg());

            //二生成静态
            generateDetailHtml(setmealDetail);
        }
    }

    //二生成静态
    private void generateDetailHtml(Setmeal setmealDetail)throws Exception{
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal_detail.ftl");

        Map<String,Object>dataMap = new HashMap<>();

        dataMap.put("setmeal",setmealDetail);

        template.process(dataMap,new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(out_put_path,"setmeal_"+setmealDetail.getId()+".html")),"utf-8")));
    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        Page<Setmeal>page =  setmealDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Setmeal>pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //先更新套餐信息
        setmealDao.update(setmeal);

        //删除套餐和组的关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());

        //重新建立多对多
        if (checkgroupIds!=null) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    @Transactional
    public void delete(int id) {
        //先查看是否有关联关系
        int num = setmealDao.findOrderCountBySetmealId(id);

        if (num>0) {
            throw new HealthException("此套餐已包含进订单中，无法删除！！！");
        }

        //先删除中间表
        setmealDao.deleteSetmealCheckGroup(id);
        //再删除套餐
        setmealDao.deleteById(id);
    }

    @Override
    public List<Setmeal> findAll() {
        //新增套餐后需要重新生成静态页面
//        generateMobileStaticHtml();
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String, Object>> setmealCount = setmealDao.findSetmealCount();
        return setmealCount;
    }
}
