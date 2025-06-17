package org.mentalizer.mailer.notifier;

import de.arthurpicht.utils.core.system.SystemUtils;
import de.arthurpicht.utils.io.maxExecutionLimiter.MaxExecutionLimiter;
import de.arthurpicht.utils.io.maxExecutionLimiter.MaxExecutionLimiterException;
import de.arthurpicht.utils.io.maxExecutionLimiter.MaxExecutionLimiters;
import de.arthurpicht.utils.io.maxExecutionLimiter.Permission;
import org.mentalizer.mailer.Mailer;

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
        MaxExecutionLimiter maxExecutionLimiter
                = MaxExecutionLimiters.perDay(7, new M7rSentNotificationsFile().asPath());
        try {
            Permission permission = maxExecutionLimiter.requestExecutionPermission();
            sendMail(mailNotification, callback);
            if (permission.isMaxExecutionReached())
                sendLimitExceededNotification(callback);
        } catch (MaxExecutionLimiterException e) {
            // din
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
            } catch (Mailer.MailerException | RuntimeException e) {
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
                "[" + SystemUtils.getHostname() + "] Daily notification limit reached.\n",
                "Maximum number of notifications per day is reached for system [" + SystemUtils.getHostname() + "].\n"
                        + "No further notifications will be sent today.\n"
                        + "Please see log files for more details.");
        sendMail(notification, callback);
    }

}
