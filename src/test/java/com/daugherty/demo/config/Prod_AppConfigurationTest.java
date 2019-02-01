package com.daugherty.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * DEVELOPER NOTE: The annotation @ActiveProfiles lets you set the current Spring profile in your unit tests. See this
 * class' base class for more info.
 */
@ActiveProfiles("prod")
public class Prod_AppConfigurationTest extends BaseAppConfigurationTest {

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    /**
     * GIVEN the application configuration file has production values for all properties
     * WHEN the properties are loaded from the application configuration
     * THEN production values should be loaded for all application properties.
     */
    @Test
    public void mustLoadProdConfiguration() {
        assertAppConfiguration("demo-prod");
    }

    // -----------------------------------------------------------------------------------------------------------------
}
