package org.mentalizer.mailer;

public class MailConfigurationOptional {

    private final MailConfiguration mailConfiguration;

    public static MailConfigurationOptional create(MailConfiguration mailConfiguration) {
        return new MailConfigurationOptional(mailConfiguration);
    }

    public static MailConfigurationOptional createAsEmpty() {
        return new MailConfigurationOptional(null);
    }

    private MailConfigurationOptional(MailConfiguration mailConfiguration) {
        this.mailConfiguration = mailConfiguration;
    }

    public boolean hasConfiguration() {
        return this.mailConfiguration != null;
    }

    public boolean isEmpty() {
        return this.mailConfiguration == null;
    }

    public MailConfiguration getMailConfiguration() {
        if (this.mailConfiguration == null) throw new IllegalStateException("Mail configuration is optionally empty.");
        return this.mailConfiguration;
    }

}
