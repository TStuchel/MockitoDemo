package com.daugherty.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * DEVELOPER NOTE: The annotation @ActiveProfiles lets you set the current Spring profile in your unit tests. See this
 * class' base class for more info.
 */
@ActiveProfiles("test")
public class Test_AppConfigurationTest extends BaseAppConfigurationTest {

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    /**
     * GIVEN the application configuration file has test values for all properties
     * WHEN the properties are loaded from the application configuration
     * THEN test values should be loaded for all application properties.
     */
    @Test
    public void mustLoadTestConfiguration() {
        assertAppConfiguration("demo-test");
    }

    // -----------------------------------------------------------------------------------------------------------------

}
