package org.mentalizer.mailer.notifier;

import org.mentalizer.mailer.Mailer;
import org.mentalizer.mailer.MailerException;
import org.mentalizer.mailer.notifier.NotificationSum.LimitStatus;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
        LimitStatus limitStatus = NotificationSum.getLimitStatus();
        if (limitStatus == LimitStatus.BELOW_LIMIT) {
            sendMail(mailNotification, callback);
        } else if (limitStatus == LimitStatus.ON_LIMIT) {
            sendMail(mailNotification, callback);
            sendLimitExceededNotification(callback);
        }
    }

    public static void sendMail(MailNotification mailNotification, MailNotifierCallback callback) {
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

    private static void sendLimitExceededNotification(MailNotifierCallback callback) {
        MailNotification notification = new MailNotification(
                "[" + getHostname() + "] Daily notification limit reached.\n",
                "Maximum number of notifications per day is reached for system [" + getHostname() + "].\n"
                        + "No further notifications will be sent today.\n"
                        + "Please see log files for more details.");
        sendMail(notification, callback);
    }

    private static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "UNKNOWN";
        }
    }

}
