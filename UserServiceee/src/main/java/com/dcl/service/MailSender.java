package com.dcl.service;

public interface MailSender {
	
	void sendMail(String to,String subject,String body);

}
