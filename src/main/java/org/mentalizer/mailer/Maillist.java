package org.mentalizer.mailer;

import de.arthurpicht.utils.io.file.TextFileUtils;
import de.arthurpicht.utils.io.nio2.FileUtils;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Maillist {

    public static class MaillistException extends Exception {
        public MaillistException(String message) {
            super(message);
        }
        public MaillistException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static List<String> read(Path maillistFile) throws MaillistException {
        if (!FileUtils.isExistingRegularFile(maillistFile))
            throw new MaillistException("Mail list file not found: [" + maillistFile.toAbsolutePath() + "].");

        List<String> recipients = readFromFile(maillistFile);
        validate(recipients);

        return recipients;
    }

    private static List<String> readFromFile(Path maillistFile) throws MaillistException {
        try {
            return TextFileUtils
                    .readNonCommentedLinesAsStrings(maillistFile, "#")
                    .stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MaillistException("Could not read mail list file [" + maillistFile.toAbsolutePath() + "]: "
                                       + e.getMessage(), e);
        }
    }

    private static void validate(List<String> recipients) throws MaillistException {
        for (String recipient : recipients) {
            try {
                new InternetAddress(recipient).validate();
            } catch (AddressException e) {
                throw new MaillistException("Invalid format of email address: [" + recipient + "].");
            }
        }
    }

}
