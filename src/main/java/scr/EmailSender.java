package scr;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
* Клас для надсилання електронних листів через SMTP (мережевий протокол для надсилання електронної пошти).
 * <p>
 * Використовує сервіс Gmail для відправки повідомлень.
 * Пароль береться зі змінної середовища для безпеки,
 * щоб не зберігати його прямо в коді.
 * </p>
 *
 * <p>Розробник: Соловій Катерина</p>
 * <p>Дата виконання: 2025-12-02</p>
 */

public class EmailSender {

    /** Електронна пошта відправника */
    private static final String USER = "soloviyk@gmail.com";

    /** Пароль від пошти, зчитується зі змінної середовища */
    private static final String PASSWORD = System.getenv("EMAIL_PASSWORD");

    /**
     * Конструктор класу EmailSender.
     * Клас не потребує ініціалізації, всі методи статичні.
     */
    public EmailSender() {}

    /**
     * Створює SMTP-сесію для Gmail.
     * <p>
     * Сесія — це підключення до сервера пошти, через яке відправляються листи.
     * Встановлює параметри сервера і авторизацію.
     * </p>
     *
     * @return об’єкт Session для роботи з Gmail
     */
    public static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");       // сервер Gmail
        props.put("mail.smtp.port", "587");                  // порт для TLS
        props.put("mail.smtp.auth", "true");                // потрібна авторизація
        props.put("mail.smtp.starttls.enable", "true");     // використовувати захищене з'єднання

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASSWORD);
            }
        });
    }

    /**
     * Створює лист (Message) без відправки.
     * <p>
     * Лист включає: відправника, отримувача, тему та текст повідомлення.
     * </p>
     *
     * @param session об’єкт сесії SMTP
     * @param to адреса отримувача
     * @param subject тема листа
     * @param body текст листа
     * @return об’єкт Message, готовий до відправки
     * @throws MessagingException якщо сталася помилка при створенні листа
     */
    public static Message createMessage(Session session, String to, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    /**
     * Відправляє лист на вказану адресу.
     * <p>
     * Створює сесію, формує лист і відправляє його через сервер Gmail.
     * Якщо під час відправки виникає помилка, викликається e.printStackTrace().
     * </p>
     * 
     * <p>
     * <b>Стек-трейс (stack trace)</b> — це детальна інформація про те,
     * де саме в коді сталася помилка. Він показує всі методи,
     * які були викликані до моменту помилки, щоб було легше знайти причину.
     * </p>
     *
     * @param to адреса отримувача
     * @param subject тема листа
     * @param body текст листа
     */
    public static void sendEmail(String to, String subject, String body) {
        try {
            Session session = createSession();                // підключаємося до сервера
            Message message = createMessage(session, to, subject, body); // формуємо лист
            Transport.send(message);                           // відправляємо лист
            System.out.println("Лист надіслано на " + to + "!");
        } catch (MessagingException e) {
            e.printStackTrace();                         
        }
    }
}
