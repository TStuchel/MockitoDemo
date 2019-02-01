package com.daugherty.demo.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A base class for testing all lifecycle configurations.
 * <p>
 * DEVELOPER NOTE: We need @ContextConfiguration so that Spring can interact with JUnit such that Spring configurations
 * will be loaded as part of this unit test.
 * <p>
 * Why do this at all? Because YAML files are easily broken! Testing our configuration makes sure that we stay honest
 * about modifying our configuration files, and that we've included every configuration for every lifecycle.
 * <p>
 * Arguably, you wouldn't want configuration files at all. Twelve-factor cloud applications should read their
 * configurations from environment variables, not from source-controlled properties files. In practice, most
 * applications respect this factor only for credentials... which is good.
 * <p>
 * !!! NEVER store usernames, passwords, or security keys in GitHub !!!
 * <p>
 * https://12factor.net/
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfiguration.class, initializers = ConfigFileApplicationContextInitializer.class)
public abstract class BaseAppConfigurationTest {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    /**
     * DEVELOPER NOTE: @Autowired here so that we let Spring *really* load the properties, and SpringExtension will let
     * Spring inject the loaded AppConfiguration into this test class.
     */
    @Autowired
    protected AppConfiguration appConfiguration;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    /**
     * GIVEN the application configuration file has values for all properties
     * WHEN the properties are loaded from the application configuration
     * THEN values should be loaded for all application properties.
     */
    protected void assertAppConfiguration(String name) {

        // THEN values should be loaded for all application properties.
        assertEquals(name, appConfiguration.getConfigName());
        assertNotNull(appConfiguration.getEnvironment());
        assertFalse(appConfiguration.getServers().isEmpty());
    }

    // -----------------------------------------------------------------------------------------------------------------

}
