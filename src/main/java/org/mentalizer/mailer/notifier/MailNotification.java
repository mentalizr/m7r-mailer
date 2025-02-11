package org.mentalizer.mailer.notifier;

import org.mentalizer.mailer.*;

import java.util.List;

public class MailNotification {

    private final String subject;
    private final String text;
    private final List<String> recipients;
    private final MailConfiguration mailConfiguration;

    public static class Builder {

        private String subject = "";
        private String text = "";

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

    }

    private MailNotification(
            String subject,
            String text
    ) {

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
        } catch (MailerException e) {
            throw new NotifierRuntimeException(e);
        }
    }

    private MailConfiguration readMailConfiguration() {
        try {
            return MailConfigurationLoader.load();
        } catch (MailConfigurationException e) {
            throw new NotifierRuntimeException(e);
        }
    }

}



