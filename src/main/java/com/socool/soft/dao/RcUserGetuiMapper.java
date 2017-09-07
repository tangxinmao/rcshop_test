package com.socool.soft.dao;

import com.socool.soft.bo.RcUserGetui;

import java.util.List;

public interface RcUserGetuiMapper {
    int deleteByPrimaryKey(Integer userGetuiId);

    int insert(RcUserGetui record);

    int insertSelective(RcUserGetui record);

    RcUserGetui selectByPrimaryKey(Integer userGetuiId);

    int updateByPrimaryKeySelective(RcUserGetui record);

    int updateByPrimaryKey(RcUserGetui record);

    List<RcUserGetui> select(RcUserGetui query);

    void deleteByMemberIdOrCID(RcUserGetui userGetui);
}