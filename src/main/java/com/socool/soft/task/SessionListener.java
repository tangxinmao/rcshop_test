package com.socool.soft.task;

import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Administrator on 2016/7/29.
 */
public class SessionListener implements HttpSessionListener {
	public static Vector<HttpSession> httpSessions = new Vector<HttpSession>();
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession httpSession = arg0.getSession();
		httpSessions.add(httpSession);
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
//		HttpSession httpSession = arg0.getSession();
	}
}
