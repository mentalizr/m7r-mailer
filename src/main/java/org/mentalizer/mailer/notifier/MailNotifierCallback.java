package org.mentalizer.mailer.notifier;

public interface MailNotifierCallback {

    void onSuccess(MailNotifierResult result);

    void onFailure(MailNotifierResult result);

}
