package org.mentalizer.mailer.notifier;

@SuppressWarnings("unused")
public class NotifierRuntimeException extends RuntimeException {

    public NotifierRuntimeException() {
    }

    public NotifierRuntimeException(String message) {
        super(message);
    }

    public NotifierRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotifierRuntimeException(Throwable cause) {
        super(cause);
    }

    public NotifierRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
