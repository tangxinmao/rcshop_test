package com.socool.soft.service;

import java.security.GeneralSecurityException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.socool.soft.vo.MailVo;

public interface IMailService {

	void sendMail(MailVo mailVo) throws GeneralSecurityException, AddressException, MessagingException;

}
