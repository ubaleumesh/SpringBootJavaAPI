package com.swnirmit.srvc;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SImplEMail implements SrvcEMail {

	@Autowired
	JavaMailSender jms;

	@Override
	public boolean unlockUserAcSendMail(String Subject, String body, String Sendto) {
		try {
			
			MimeMessage MailMsg = jms.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(MailMsg);
			helper.setTo(Sendto);
			helper.setSubject(Subject);
			helper.setText(body,true);
			jms.send(MailMsg);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
