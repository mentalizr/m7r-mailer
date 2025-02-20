package org.mentalizer.mailer.notifier;

import de.arthurpicht.utils.io.file.TextFileUtils;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SentNotifications {

    private static final Path SENT_NOTIFICATIONS_PATH = new M7rSentNotificationsFile().asPath();

    public static synchronized List<LocalDateTime> getAll() {
        if (!FileUtils.isExistingRegularFile(SENT_NOTIFICATIONS_PATH))
            return new ArrayList<>();

        List<String> lines;
        try {
            lines = TextFileUtils.readLinesAsStrings(SENT_NOTIFICATIONS_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Error accessing file [" + SENT_NOTIFICATIONS_PATH.toAbsolutePath() + "].", e);
        }
        List<LocalDateTime> notificationTimestamps = new ArrayList<>();
        for (String line : lines) {
            LocalDateTime timestamp = LocalDateTime.parse(line);
            notificationTimestamps.add(timestamp);
        }
        return notificationTimestamps;
    }

    public static synchronized int size() {
        return getAll().size();
    }

    public synchronized static void add(LocalDateTime timestamp) {
        String timestampString = timestamp.toString();
        try {
            TextFileUtils.appendLine(SENT_NOTIFICATIONS_PATH, timestampString);
        } catch (IOException e) {
            throw new RuntimeException("Error accessing file [" + SENT_NOTIFICATIONS_PATH.toAbsolutePath() + "].", e);
        }
    }

    public synchronized static void set(List<LocalDateTime> notificationTimestamps) {
        List<String> timestamps = new ArrayList<>();
        for (LocalDateTime timestamp : notificationTimestamps) {
            timestamps.add(timestamp.toString());
        }
        try {
            Files.write(SENT_NOTIFICATIONS_PATH, timestamps);
        } catch (IOException e) {
            throw new RuntimeException("Error accessing file [" + SENT_NOTIFICATIONS_PATH.toAbsolutePath() + "].", e);
        }
    }

}
