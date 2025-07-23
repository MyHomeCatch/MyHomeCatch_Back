package org.scoula.email.service;

import javax.mail.internet.MimeMessage;

public interface MailService {

    public MimeMessage createMail(String mail);
    public int sendMail(String mail);
}
