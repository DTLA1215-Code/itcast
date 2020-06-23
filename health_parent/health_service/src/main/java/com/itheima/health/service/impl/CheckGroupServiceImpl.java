package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);

        Integer checkGroupId = checkGroup.getId();
        if (null != checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkitemId);
            }
        }
    }

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        // 使用PageHelper.startPage
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 有查询条件的处理, 模糊查询
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString()+ "%");
        }
        // 紧接着的查询会被分页
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<CheckGroup>(page.getTotal(), page.getResult());
    }
}
