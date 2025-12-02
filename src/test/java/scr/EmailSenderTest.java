package scr;

import org.junit.jupiter.api.Test;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import static org.junit.jupiter.api.Assertions.*;

public class EmailSenderTest {

    @Test
    public void testCreateSession() {
        Session session = EmailSender.createSession();
        assertNotNull(session, "Сесія не повинна бути null");
        assertTrue(session.getProperties().getProperty("mail.smtp.host").equals("smtp.gmail.com"));
    }

    @Test
    public void testCreateMessage() throws MessagingException {
        Session session = EmailSender.createSession();
        String to = "test@gmail.com";
        String subject = "Тестова тема";
        String body = "Текст листа";

        Message message = EmailSender.createMessage(session, to, subject, body);

        assertEquals(to, message.getRecipients(Message.RecipientType.TO)[0].toString(), "Адресат не збігається");
        assertEquals(subject, message.getSubject(), "Тема листа не збігається");
    }

    // Цей тест не буде реально відправляти лист, а просто перевіряє, що метод викликається
    @Test
    public void testSendEmailDoesNotThrow() {
        assertDoesNotThrow(() -> {
            EmailSender.sendEmail("test@gmail.com", "Тест", "Це тестовий лист");
        }, "Метод sendEmail не повинен кидати виключення");
    }
}
