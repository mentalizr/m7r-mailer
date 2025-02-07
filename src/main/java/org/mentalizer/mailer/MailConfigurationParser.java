package org.mentalizer.mailer;

import de.arthurpicht.configuration.Configuration;

public class MailConfigurationParser {

    private static final String SENDER = "sender";
    private static final String SMTP_SERVER = "smtp_server";
    private static final String SMTP_SERVER_PORT = "smtp_server_port";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String DEBUG = "debug";

    public static MailConfiguration parse(Configuration configuration)
            throws MailConfigurationException {

        if (!configuration.containsKey(SENDER))
            throw new MailConfigurationException(getKeyMissingExceptionMessage(SENDER));
        String sender = configuration.getString(SENDER);

        if (!configuration.containsKey(SMTP_SERVER))
            throw new MailConfigurationException(getKeyMissingExceptionMessage(SMTP_SERVER));
        String smtpServer = configuration.getString(SMTP_SERVER);

        boolean debug = configuration.getBoolean(DEBUG, false);

        if (configuration.containsKey(USER) && !configuration.containsKey(PASSWORD))
            throw new MailConfigurationException(getCredentialsIncompleteExceptionMessage(USER, PASSWORD));
        if (!configuration.containsKey(USER) && configuration.containsKey(PASSWORD))
            throw new MailConfigurationException(getCredentialsIncompleteExceptionMessage(PASSWORD, USER));
        if (configuration.containsKey(USER)) {
            String user = configuration.getString(USER);
            String password = configuration.getString(PASSWORD);
            int port = configuration.getInt(SMTP_SERVER_PORT, 587);
            return new MailConfiguration(sender, smtpServer, port, user, password, debug);
        } else {
            int port = configuration.getInt(SMTP_SERVER_PORT, 25);
            return new MailConfiguration(sender, smtpServer, port, debug);
        }
    }

    private static String getKeyMissingExceptionMessage(String key) {
        return "No configuration found for key [" + key + "] in [mailer.config].";
    }

    private static String getCredentialsIncompleteExceptionMessage(String given, String missing) {
        return "Configuration of mailer credentials in  [mailer.config] is incomplete. [" + given + "] is specified" +
               " but value for key [" + missing + "] is missing.";
    }

}
