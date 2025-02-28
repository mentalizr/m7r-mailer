package org.mentalizer.mailer.notifier;

import org.mentalizer.mailer.*;

import java.util.List;

public class MailNotification {

    public static class MailNotificationRuntimeException extends RuntimeException {
        public MailNotificationRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private final String subject;
    private final String text;
    private final List<String> recipients;
    private final MailConfiguration mailConfiguration;

    public MailNotification (
            String subject,
            String text
    ) throws MailNotificationRuntimeException {

        if (subject == null || subject.isEmpty())
            throw new IllegalArgumentException("subject cannot be null or empty.");
        if (text == null || text.isEmpty())
            throw new IllegalArgumentException("text cannot be null or empty.");

        this.subject = subject;
        this.text = text;
        this.recipients = readMaillist();
        this.mailConfiguration = readMailConfiguration();
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public MailConfiguration getMailConfiguration() {
        return mailConfiguration;
    }

    private List<String> readMaillist() {
        try {
            return Maillist.read(new M7rNotificationRecipientsFile().asPath());
        } catch (Maillist.MaillistException e) {
            throw new MailNotificationRuntimeException(e.getMessage(), e);
        }
    }

    private MailConfiguration readMailConfiguration() {
        try {
            return MailConfigurationLoader.load();
        } catch (MailConfigurationException e) {
            throw new MailNotificationRuntimeException(e.getMessage(), e);
        }
    }

}



