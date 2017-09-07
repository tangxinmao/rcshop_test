package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderFee;

public interface RcOrderFeeMapper {

	int deleteByPrimaryKey(long orderFeeId);

	int insert(RcOrderFee record);

//	RcOrderFee selectByPrimaryKey(long orderFeeId);

	int updateByPrimaryKey(RcOrderFee record);

	List<RcOrderFee> select(RcOrderFee record);

	RcOrderFee selectOne(RcOrderFee record);

	int delete(RcOrderFee record);
}