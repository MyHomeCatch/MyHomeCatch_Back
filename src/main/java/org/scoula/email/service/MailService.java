package org.scoula.email.service;

import org.scoula.email.dto.EmailDTO;
import org.scoula.email.dto.EmailRequestDto;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

public interface MailService {

    public MimeMessage createMail(String mail);
    public int sendMail(String mail);
    public EmailDTO mailCheck(EmailRequestDto requestDto, HttpSession session);
}
