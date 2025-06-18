package org.mentalizer.mailer.notifier;

import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.host.hostDir.M7rHostRunDir;

public class M7rSentNotificationsFile extends M7rFile {

    public M7rSentNotificationsFile() {
        super(new M7rHostRunDir(), "sent-notifications.timestamp");
    }

}
