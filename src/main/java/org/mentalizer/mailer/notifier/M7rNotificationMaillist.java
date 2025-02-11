package org.mentalizer.mailer.notifier;

import org.mentalizer.mailer.MailerException;
import org.mentalizer.mailer.Maillist;

import java.util.List;

public class M7rNotificationMaillist {

    public static List<String> read() {
        try {
            return Maillist.read(new M7rNotificationRecipientsFile().asPath());
        } catch (MailerException e) {
            throw new NotifierRuntimeException(e);
        }
    }

}
