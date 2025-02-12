package org.mentalizer.mailer.notifier;

import org.mentalizer.mailer.Mailer;
import org.mentalizer.mailer.MailerException;

@SuppressWarnings("unused")
public class MailNotifier {

    public static void sendNotification(String subject, String text) {
        sendNotification(new MailNotification(subject, text));
    }

    public static void sendNotification(String subject, String text, MailNotifierCallback callback) {
        sendNotification(new MailNotification(subject, text), callback);
    }

    public static void sendNotification(MailNotification mailNotification) {
        sendNotification(mailNotification, null);
    }

    public static void sendNotification(MailNotification mailNotification, MailNotifierCallback callback) {
        Thread thread = new Thread(() -> {
            try {
                Mailer.sendPlainTextMail(
                        mailNotification.getSubject(),
                        mailNotification.getText(),
                        mailNotification.getRecipients(),
                        mailNotification.getMailConfiguration());
                if (callback != null) {
                    MailNotifierResult result = new MailNotifierResult(mailNotification);
                    callback.onSuccess(result);
                }
            } catch (MailerException | RuntimeException e) {
                if (callback != null) {
                    MailNotifierResult result = new MailNotifierResult(mailNotification, e);
                    callback.onFailure(result);
                }
            }
        });
        thread.start();
    }

}
