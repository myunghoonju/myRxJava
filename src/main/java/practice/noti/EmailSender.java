package practice.noti;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    public static final String[] MAILING_LIST = {"myunghoonju@naver.com"};
    public static final String[] MAILING_CC_LIST = {"myunghoonju@naver.com"};

    private final JavaMailSender javaMailSender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void mailSend() {
        SimpleMailMessage textMsg = new SimpleMailMessage();

        textMsg.setFrom("myunghoonju@gmail.com");
        textMsg.setTo(MAILING_LIST);
        textMsg.setCc(MAILING_CC_LIST);
        textMsg.setText("Hello \n\n Everyone! \n\n How's your day?");
        textMsg.setSubject("Stopping by");

        javaMailSender.send(textMsg);
    }
}
