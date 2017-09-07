package com.socool.soft.listener;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimeZoneListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
	}
}
