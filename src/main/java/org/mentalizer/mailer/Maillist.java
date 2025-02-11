package org.mentalizer.mailer;

import de.arthurpicht.utils.io.file.TextFileUtils;
import de.arthurpicht.utils.io.nio2.FileUtils;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Maillist {

    public static List<String> read(Path maillistFile) throws MailerException {
        if (!FileUtils.isExistingRegularFile(maillistFile))
            throw new MailerException("Mail list file not found: [" + maillistFile.toAbsolutePath() + "].");

        List<String> recipients = readFromFile(maillistFile);
        validate(recipients);

        return recipients;
    }

    private static List<String> readFromFile(Path maillistFile) throws MailerException {
        try {
            return TextFileUtils.readNonCommentedLinesAsStrings(maillistFile, "#");
        } catch (IOException e) {
            throw new MailerException("Could not read mail list file [" + maillistFile.toAbsolutePath() + "]: "
                                       + e.getMessage(), e);
        }
    }

    private static void validate(List<String> recipients) throws MailerException {
        for (String recipient : recipients) {
            try {
                new InternetAddress(recipient).validate();
            } catch (AddressException e) {
                throw new MailerException("Invalid format of email address: [" + recipient + "].");
            }
        }
    }

}


