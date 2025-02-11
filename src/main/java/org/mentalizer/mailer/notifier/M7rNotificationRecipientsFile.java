package org.mentalizer.mailer.notifier;

import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.host.hostDir.M7rHostConfigDir;

public class M7rNotificationRecipientsFile extends M7rFile {

    public M7rNotificationRecipientsFile() {
        super(new M7rHostConfigDir(), "notificationRecipients.maillist");
    }

}
