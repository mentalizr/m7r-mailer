package org.mentalizer.mailer;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.mentalizr.commons.paths.client.M7rMailerConfigFile;

import java.io.IOException;
import java.nio.file.Path;

public class MailConfigurationLoader {

    public static MailConfiguration load() throws MailConfigurationException {
        M7rMailerConfigFile configFile = M7rMailerConfigFile.createInstance();
        return load(configFile.asPath());
    }

    public static MailConfiguration load(Path mailConfigFile) throws MailConfigurationException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        bindConfigFile(configurationFactory, mailConfigFile);
        Configuration configuration = configurationFactory.getConfiguration();
        return MailConfigurationParser.parse(configuration);
    }

    private static void bindConfigFile(ConfigurationFactory configurationFactory, Path mailConfigFile)
            throws MailConfigurationException {

        try {
            configurationFactory.addConfigurationFileFromFilesystem(mailConfigFile.toFile());
        } catch (ConfigurationFileNotFoundException | IOException e) {
            throw new MailConfigurationException("Mail configuration file not found: "
                                                 + mailConfigFile.toAbsolutePath());
        }
    }

}
