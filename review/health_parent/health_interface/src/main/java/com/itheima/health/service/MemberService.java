package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

public interface MemberService {
    void add(Member member);

    Member findByTelephone(String telephone);

    List<Integer> findMembersByMonths(List<String> months);

}
