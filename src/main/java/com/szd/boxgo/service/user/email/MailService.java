package com.szd.boxgo.service.user.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String email, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Подтверждение регистрации GO BOX");

            String htmlContent = """
                    <html>
                        <body>
                            <p>Здравствуйте!</p>
                            <p/>
                            <p>Спасибо за регистрацию в приложении GO BOX!</p>
                            <p>Чтобы подтвердить Вашу электронную почту, пожалуйста, введите следующий код подтверждения:</p>
                            <p/>
                            <p><div style="border: 2px dashed #4CAF50; padding: 10px; font-size: 18px; font-weight: bold; color: #333; background-color: #f9f9f9; text-align: center;">%s</div></p>
                            <p/>
                            <p>Код действителен в течение 10 минут.</p>
                            <p/>
                            <p>Если Вы не регистрировались в GO BOX, просто проигнорируйте это письмо.</p>
                            <p>Никаких действий предпринимать не нужно.</p>
                            <p/>
                            <p>С наилучшими пожеланиями,<br>Команда GO BOX</p>
                        </body>
                    </html>
                    """.formatted(code);

            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }

    public void sendResetPassword(String email, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Восстановление пароля в приложении Go BOX");

            String htmlContent = """
                    <html>
                        <body>
                            <p>Здравствуйте!</p>
                            <p/>
                            <p>Вы запросили восстановление пароля в приложении Go BOX.</p>
                            <p>Пожалуйста, введите следующий код для продолжения:</p>
                            <p/>
                            <p><div style="border: 2px dashed #4CAF50; padding: 10px; font-size: 18px; font-weight: bold; color: #333; background-color: #f9f9f9; text-align: center;">%s</div></p>
                            <p/>
                            <p>Код действителен в течение 10 минут.</p>
                            <p/>
                            <p>Если вы не запрашивали восстановление пароля, просто проигнорируйте это письмо.</p>
                            <p>Никаких действий предпринимать не нужно.</p>
                            <p/>
                            <p>С наилучшими пожеланиями,<br>Команда GO BOX</p>
                        </body>
                    </html>
                    """.formatted(code);

            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }

    public void sendChangePassword(String email, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Подтверждение смены пароля в приложении Go BOX");

            String htmlContent = """
                    <html>
                        <body>
                            <p>Здравствуйте!</p>
                            <p/>
                            <p>Вы запросили смену пароля в приложении Go BOX.</p>
                            <p>Пожалуйста, введите следующий код подтверждения:</p>
                            <p/>
                            <p><div style="border: 2px dashed #4CAF50; padding: 10px; font-size: 18px; font-weight: bold; color: #333; background-color: #f9f9f9; text-align: center;">%s</div></p>
                            <p/>
                            <p>Код действителен в течение 10 минут.</p>
                            <p/>
                            <p>Если вы не запрашивали восстановление пароля, просто проигнорируйте это письмо.</p>
                            <p>Никаких действий предпринимать не нужно.</p>
                            <p/>
                            <p>С наилучшими пожеланиями,<br>Команда GO BOX</p>
                        </body>
                    </html>
                    """.formatted(code);

            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }
}
