package org.mentalizer.mailer.notifier;

public class MailNotifierResult {

    private final MailNotification mailNotification;
    private final Throwable throwable;

    public MailNotifierResult(MailNotification mailNotification) {
        this.mailNotification = mailNotification;
        this.throwable = null;
    }

    public MailNotifierResult(MailNotification mailNotification, Throwable throwable) {
        this.mailNotification = mailNotification;
        this.throwable = throwable;
    }

    public MailNotification getMailNotification() {
        return mailNotification;
    }

    public boolean hasThrowable() {
        return throwable != null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
