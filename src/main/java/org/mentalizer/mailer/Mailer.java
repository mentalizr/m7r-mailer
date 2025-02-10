package org.mentalizer.mailer;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Mailer {

    public static void sendPlainTextMail(
            String subject,
            String text,
            List<String> recipients,
            MailConfiguration mailConfiguration)
            throws MailerException {

        if (recipients.isEmpty()) return;

        Session session = obtainSession(mailConfiguration);
        Message message = createPlainTextMessage(session, mailConfiguration, subject, text, recipients);
        sendMessage(message);
    }

    public static void sendMimeMultipartMailWithAttachment(
            String subject,
            String textBody,
            List<String> recipients,
            Path attachmentPath,
            MailConfiguration mailConfiguration)
            throws MailerException {

        if (recipients.isEmpty()) return;

        Session session = obtainSession(mailConfiguration);
        Message message = createMimeMultipartMessageWithAttachment(session, mailConfiguration, textBody, subject,
                recipients, attachmentPath);
        sendMessage(message);
    }

    private static Session obtainSession(MailConfiguration mailConfiguration) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailConfiguration.getSmtpServer());
        properties.put("mail.debug", mailConfiguration.isDebug());
        properties.put("mail.smtp.port", mailConfiguration.getSmtpServerPort());

        if (mailConfiguration.hasCredentials()) {
            return getAuthenticatedSession(mailConfiguration, properties);
        } else {
            return getUnauthenticatedSession(properties);
        }
    }

    private static Session getAuthenticatedSession(MailConfiguration mailConfiguration, Properties properties) {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        mailConfiguration.getUser(),
                        mailConfiguration.getPassword()
                );
            }
        };
        return Session.getInstance(properties, authenticator);
    }

    private static Session getUnauthenticatedSession(Properties properties) {
        return Session.getInstance(properties);
    }

    private static Message createPlainTextMessage(
            Session session,
            MailConfiguration mailConfiguration,
            String subject,
            String text,
            List<String> recipients) throws MailerException {

        try {
            Message message = new MimeMessage(session);
            addHeaderData(message, subject, mailConfiguration, recipients);
            message.setText(text);
            return message;
        } catch (MessagingException e) {
            throw new MailerException("Error creating mail message: " + e.getMessage(), e);
        }
    }

    private static Message createMimeMultipartMessageWithAttachment(
            Session session,
            MailConfiguration mailConfiguration,
            String text,
            String subject,
            List<String> recipients,
            Path attachmentPath) throws MailerException {

        try {
            Message message = new MimeMessage(session);
            addHeaderData(message, subject, mailConfiguration, recipients);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachmentPath.toFile());

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(text);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentPart);
            multipart.addBodyPart(textPart);

            message.setContent(multipart);
            return message;
        } catch (MessagingException | IOException e) {
            throw new MailerException("Error creating mail message: " + e.getMessage(), e);
        }
    }

    private static void addHeaderData(Message message, String subject, MailConfiguration mailConfiguration, List<String> recipients) throws MessagingException {
        message.setFrom(new InternetAddress(mailConfiguration.getSender()));
        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
        message.setSubject(subject);
        Date now = Date.from(LocalDateTime.now(Clock.systemUTC()).toInstant(ZoneOffset.UTC));
        message.setSentDate(now);
    }

    private static void sendMessage(Message message) throws MailerException {
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MailerException("Error sending mail: " + e.getMessage(), e);
        }
    }

}
