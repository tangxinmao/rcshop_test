package com.socool.soft.vo;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;

public class MailVo {
	private HttpServletRequest httpServletRequest;
	private String to;//收件人
	private VelocityContext context=new VelocityContext();
	private String velocity;//模板模板
	private String subject;//邮件标题
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public VelocityContext getContext() {
		return context;
	}
	public void setContext(VelocityContext context) {
		this.context = context;
	}
	public String getVelocity() {
		return velocity;
	}
	public void setVelocity(String velocity) {
		this.velocity = velocity;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	

}
