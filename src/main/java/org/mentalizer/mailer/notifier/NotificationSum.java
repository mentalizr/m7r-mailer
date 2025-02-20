package org.mentalizer.mailer.notifier;

import de.arthurpicht.utils.core.dates.LocalDateTimes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationSum {

    public enum LimitStatus {BELOW_LIMIT, ON_LIMIT, ABOVE_LIMIT}

    private static final int MAX_NR_OF_NOTIFICATIONS_PER_DAY = 7;

    public static synchronized LimitStatus getLimitStatus() {
        removeObsoleteTimestamps();
        SentNotifications.add(LocalDateTime.now());
        if (SentNotifications.size() < MAX_NR_OF_NOTIFICATIONS_PER_DAY) {
            return LimitStatus.BELOW_LIMIT;
        } else if (SentNotifications.size() == MAX_NR_OF_NOTIFICATIONS_PER_DAY) {
            return LimitStatus.ON_LIMIT;
        } else {
            return LimitStatus.ABOVE_LIMIT;
        }
    }

    private static void removeObsoleteTimestamps() {
        LocalDateTime beginOfCurrentDay = LocalDateTimes.calculateBeginOfDayFromCurrent(0);
        List<LocalDateTime> notificationTimestamps = SentNotifications.getAll();
        List<LocalDateTime> notificationTimestampsCleaned = new ArrayList<>();
        for (LocalDateTime notificationTimestamp : notificationTimestamps) {
            if (notificationTimestamp.isAfter(beginOfCurrentDay))
                notificationTimestampsCleaned.add(notificationTimestamp);
        }
        SentNotifications.set(notificationTimestampsCleaned);
    }

}
