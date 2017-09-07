package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcCity extends QueryParam implements Serializable {

	private static final long serialVersionUID = 1924185258879214953L;
	
	// db
	private Integer cityId;
	private String provinceName;
	private String name;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal freight;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal freightBaseAmount;
	private Byte isDredged;
	private Byte isHot;
	private Date dredgeTime;
	private Byte isDefault;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getFreightBaseAmount() {
		return freightBaseAmount;
	}

	public void setFreightBaseAmount(BigDecimal freightBaseAmount) {
		this.freightBaseAmount = freightBaseAmount;
	}

	public Byte getIsDredged() {
		return isDredged;
	}

	public void setIsDredged(Byte isDredged) {
		this.isDredged = isDredged;
	}

	public Byte getIsHot() {
		return isHot;
	}

	public void setIsHot(Byte isHot) {
		this.isHot = isHot;
	}

	public Date getDredgeTime() {
		return dredgeTime;
	}

	public void setDredgeTime(Date dredgeTime) {
		this.dredgeTime = dredgeTime;
	}

	public Byte getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}
}