package com.daugherty.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * A base testing class to contain common test class functionality.
 */
public abstract class BaseTest {

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Used for random test values
     */
    protected static final PodamFactory podamFactory = new PodamFactoryImpl();

    /**
     * Serialization/Deserialization
     */
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    // -----------------------------------------------------------------------------------------------------------------

}
