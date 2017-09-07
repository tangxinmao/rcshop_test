package com.socool.soft.service.impl;

import com.socool.soft.bo.RcUserGetui;
import com.socool.soft.dao.RcUserGetuiMapper;
import com.socool.soft.service.IUserGetuiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserGetuiServiceImpl implements IUserGetuiService {
    @Autowired
    private RcUserGetuiMapper userGetuiMapper;

    @Override
    public void userBind(Integer memberId, String cid) {
        RcUserGetui userGetui = new RcUserGetui();
        userGetui.setMemberId(memberId);
        userGetui.setCid(cid);
        userGetui.setUpdateTime(new Date());

        // 先删除该memberId对应的记录 和 cid对应的记录
        userGetuiMapper.deleteByMemberIdOrCID(userGetui);
        // 添加关联信息
        userGetuiMapper.insertSelective(userGetui);
    }

    @Override
    public RcUserGetui getByMemberId(Integer memberId) {
        RcUserGetui query = new RcUserGetui();
        query.setMemberId(memberId);
        List<RcUserGetui> userGetuiList = userGetuiMapper.select(query);
        if(userGetuiList.size() == 0) {
            return null;
        }
        return userGetuiList.get(0);
    }
}
