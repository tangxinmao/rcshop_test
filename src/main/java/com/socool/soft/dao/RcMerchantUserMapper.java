package com.socool.soft.dao;

import com.socool.soft.bo.RcMerchantUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RcMerchantUserMapper {

//	int deleteByPrimaryKey(int memberId);

	int insert(RcMerchantUser record);

	RcMerchantUser selectByPrimaryKey(int memberId);

	int updateByPrimaryKey(RcMerchantUser record);

	List<RcMerchantUser> select(RcMerchantUser record);

	RcMerchantUser selectOne(RcMerchantUser record);

    RcMerchantUser selectByAccountAndPassword(@Param("account") String account, @Param("password") String password);

//	int delete(RcMerchantUser record);
}