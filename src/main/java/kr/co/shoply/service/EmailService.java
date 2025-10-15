package kr.co.shoply.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import kr.co.shoply.dto.SessionDTO;
import kr.co.shoply.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SessionDTO sessionData;
    private final MemberMapper memberMapper;

    @Autowired
    private HttpSession session;

    @Value("${spring.mail.username}")
    private String sender;

    // 이메일 확인 (회원가입용)
    public int countEmail(String mem_email) {
        return memberMapper.checkRegEmail(mem_email);
    }

    // 이름 + 이메일 확인 (아이디 찾기용)
    public int countUser(String mem_name, String mem_email) {
        return memberMapper.checkEmail(mem_name, mem_email);
    }

    // 아이디 + 이메일 확인 (비밀번호 찾기용)
    public int countUserByIdAndEmail(String mem_id, String mem_email) {
        return memberMapper.checkEmailById(mem_id, mem_email);
    }

    //이메일 인증코드 발송
    public void sendCode(String receiver){
        MimeMessage message = mailSender.createMimeMessage();

        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        String title = "Shoply 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.</h1>";

        try {
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            //메일전송
            mailSender.send(message);

            //현재 세션 저장
            //session.setAttribute("sessCode", String.valueOf(code));
            sessionData.setCode(String.valueOf(code));

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public boolean verifyCode(String code){
        //현재 세션 코드 가져오기
        //String sessCode = (String) session.getAttribute("sessCode");
        String sessCode = sessionData.getCode();

        if(sessCode.equals(code)) return true;

        return false;
    }
}
