package org.scoula.email.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.scoula.auth.dto.AuthResponse;
import org.scoula.email.dto.EmailDTO;
import org.scoula.email.dto.EmailRequestDto;
import org.scoula.email.service.MailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/email")
@Api(tags = "이메일 인증 API")
public class EmailController {
    private final MailServiceImpl mailServiceImpl;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    public EmailController(MailServiceImpl mailServiceImpl) {
        this.mailServiceImpl = mailServiceImpl;
    }

    // 인증 이메일 전송
    @ApiOperation(value = "이메일 인증번호 전송", notes = "인증번호를 이메일로 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @PostMapping("/send")
    public ResponseEntity<EmailDTO> mailSend(@RequestBody EmailRequestDto emailRequestDto, HttpSession session) {
        EmailDTO emailDTO;
        String email = emailRequestDto.getEmail();

        try {
            number = mailServiceImpl.sendMail(email);
            session.setMaxInactiveInterval(60 * 3); // 세션 저장 시간 3분 설정
            session.setAttribute("email", email);
            session.setAttribute("authCode", number); // 이메일 인증 코드 세션에 저장
            String num = String.valueOf(number);

            emailDTO = EmailDTO.builder()
                    .email(email)
                    .isSuccess(true)
                    .message("인증번호 전송 성공")
                    .build();
            return ResponseEntity.ok(emailDTO);
        } catch (Exception e) {
            e.printStackTrace();
            emailDTO = EmailDTO.builder()
                    .email(email)
                    .isSuccess(false)
                    .message("인증번호 전송 실패")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emailDTO);
        }
    }

    // 인증번호 일치여부 확인
    @ApiOperation(value = "이메일 인증번호 검증", notes = "해당 이메일로 전송된 인증번호를 검증합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @PostMapping("/check")
    public ResponseEntity<EmailDTO> mailCheck(
            @RequestBody EmailRequestDto requestDto,
            HttpSession session) {

        try {
            String email = (String) session.getAttribute("email");
            Object objCode = session.getAttribute("authCode");

            if (email == null || objCode == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        EmailDTO.builder()
                                .email(requestDto.getEmail())
                                .isSuccess(false)
                                .message("세션 정보가 존재하지 않습니다. (만료되었거나 전송되지 않음)")
                                .build()
                );
            }
            int code = (int) objCode;

            boolean isEmailMatch = requestDto.getEmail().equals(email);
            boolean isCodeMatch = requestDto.getCode() == code? true: false;

            if (!isEmailMatch) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        EmailDTO.builder()
                                .email(requestDto.getEmail())
                                .isSuccess(false)
                                .message("요청된 이메일이 일치하지 않습니다.")
                                .build()
                );
            }

            if (!isCodeMatch) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        EmailDTO.builder()
                                .email(requestDto.getEmail())
                                .isSuccess(false)
                                .message("인증번호가 일치하지 않습니다.")
                                .build()
                );
            }

            session.removeAttribute("email");
            session.removeAttribute("authCode");

            // 인증 성공
            return ResponseEntity.ok(
                    EmailDTO.builder()
                            .email(requestDto.getEmail())
                            .isSuccess(true)
                            .message("이메일 인증이 완료되었습니다.")
                            .build()
            );

        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    EmailDTO.builder()
                            .email(requestDto.getEmail())
                            .isSuccess(false)
                            .message("에러 발생")
                            .build());
        }
    }


}
