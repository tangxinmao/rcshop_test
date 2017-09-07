package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcUserGetui implements Serializable {
	
	private static final long serialVersionUID = 8279465043472349064L;

	private Integer userGetuiId;

    private Integer memberId;

    private String cid;

    private Date updateTime;

    public RcUserGetui(Integer userGetuiId) {
        this.userGetuiId = userGetuiId;
    }

    public RcUserGetui() {

    }

    public Integer getUserGetuiId() {
        return userGetuiId;
    }

    public void setUserGetuiId(Integer userGetuiId) {
        this.userGetuiId = userGetuiId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid == null ? null : cid.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}