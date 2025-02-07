package org.mentalizer.mailer;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class MailConfiguration {

    private final String sender;
    private final String smtpServer;
    private final int smtpServerPort;
    private final String user;
    private final String password;
    private final boolean debug;

    public MailConfiguration(String sender, String smtpServer, int port, String user, String password, boolean debug) {
        assertArgumentNotNull("sender", sender);
        assertArgumentNotNull("smtpServer", smtpServer);
        assertArgumentNotNull("user", user);
        assertArgumentNotNull("password", password);
        this.sender = sender;
        this.smtpServer = smtpServer;
        this.smtpServerPort = port;
        this.user = user;
        this.password = password;
        this.debug = debug;
    }

    public MailConfiguration(String sender, String smtpServer, int port, boolean debug) {
        this.sender = sender;
        this.smtpServer = smtpServer;
        this.smtpServerPort = port;
        this.user = null;
        this.password = null;
        this.debug = debug;
    }

    public String getSender() {
        return this.sender;
    }

    public String getSmtpServer() {
        return this.smtpServer;
    }

    public boolean hasCredentials() {
        return this.user != null && this.password != null;
    }

    public String getUser() {
        if (this.user == null) throw new IllegalStateException("No credentials specified. Check before calling.");
        return this.user;
    }

    public String getPassword() {
        if (this.password == null) throw new IllegalStateException("No credentials specified. Check before calling.");
        return this.password;
    }

    public int getSmtpServerPort() {
        return this.smtpServerPort;
    }

    public boolean isDebug() {
        return this.debug;
    }

}
