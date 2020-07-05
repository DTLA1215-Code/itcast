package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//MemberServiceImpl实现类	调用memberDao
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public List<Integer> findMembersByMonths(List<String> months) {
        List<Integer>memberCount = new ArrayList<>();

        if (months!=null){
            for (String month : months) {
                Integer member = memberDao.findMemberCountBeforeDate(month+"-31");
                memberCount.add(member);
            }
            return memberCount;
        }
        return null;
    }
}
