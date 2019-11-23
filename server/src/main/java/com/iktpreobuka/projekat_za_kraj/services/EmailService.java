package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.models.EmailObject;

public interface EmailService {
	
	void sendSimpleMessage(EmailObject object);
	void sendTemplateMessage(EmailObject object) throws Exception;
	void sendMessageWithAttachment(EmailObject object, String pathToAttachment) throws Exception;

}
