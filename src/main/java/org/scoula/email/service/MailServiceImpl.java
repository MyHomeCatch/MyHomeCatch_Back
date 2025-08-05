package org.scoula.email.service;

import lombok.RequiredArgsConstructor;
import org.scoula.email.dto.EmailDTO;
import org.scoula.email.dto.EmailRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private static int number;

    // 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    @Override
    public MimeMessage createMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    MimeMessage.RecipientType.TO,
                    InternetAddress.parse(mail)
            );
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    @Override
    public int sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);

        return number;
    }

    @Override
    public EmailDTO mailCheck(EmailRequestDto requestDto, HttpSession session) {
        try {
            String email = (String) session.getAttribute("email");
            Object objCode = session.getAttribute("authCode");

            if (email == null || objCode == null) {
                return EmailDTO.builder()
                                .email(requestDto.getEmail())
                                .isSuccess(false)
                                .message("세션 정보가 존재하지 않습니다. (만료되었거나 전송되지 않음)")
                                .build();

            }
            int code = (int) objCode;

            boolean isEmailMatch = requestDto.getEmail().equals(email);
            boolean isCodeMatch = requestDto.getCode() == code? true: false;

            if (!isEmailMatch) {
                return EmailDTO.builder()
                                .email(requestDto.getEmail())
                                .isSuccess(false)
                                .message("요청된 이메일이 일치하지 않습니다.")
                                .build();
            }

            if (!isCodeMatch) {
                return EmailDTO.builder()
                                .email(requestDto.getEmail())
                                .isSuccess(false)
                                .message("인증번호가 일치하지 않습니다.")
                                .build();

            }

            session.removeAttribute("email");
            session.removeAttribute("authCode");

            // 인증 성공
            return EmailDTO.builder()
                            .email(requestDto.getEmail())
                            .isSuccess(true)
                            .message("이메일 인증이 완료되었습니다.")
                            .build();


        }
        catch (Exception e) {
            e.printStackTrace();
            return EmailDTO.builder()
                            .email(requestDto.getEmail())
                            .isSuccess(false)
                            .message("에러 발생")
                            .build();
        }
    }

    @Override
    public void sendCustomAlertMail(String toEmail, String danziTitle, String type) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("[청약 알림] " + danziTitle + " - " + type);

            String body = "<h3>[" + danziTitle + "]</h3>"
                    + "<p>오늘은 '" + type + "' 입니다.</p>"
                    + "<p>청약 일정을 꼭 확인하세요.</p>";

            message.setText(body, "UTF-8", "html");
            javaMailSender.send(message);

            System.out.println("[이메일 알림] 수신자: " + toEmail + ", 제목: " + danziTitle + ", 종류: " + type);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
