package org.mentalizer.mailer;

public class MailConfigurationException extends Exception {

    public MailConfigurationException() {
    }

    public MailConfigurationException(String message) {
        super(message);
    }

    public MailConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailConfigurationException(Throwable cause) {
        super(cause);
    }

    public MailConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
