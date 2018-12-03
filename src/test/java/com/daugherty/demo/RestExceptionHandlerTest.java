package com.daugherty.demo;

import com.daugherty.demo.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class RestExceptionHandlerTest extends BaseTest {

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    private RestExceptionHandler restExceptionHandler_spy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @BeforeEach
    void setUp() {
        restExceptionHandler_spy = spy(new RestExceptionHandler());
    }

    /**
     * GIVEN a controller method is called
     * WHEN a BusinessException is thrown
     * THEN a response should be returned
     * AND it should indicate an HTTP status code of 400 - Bad Request
     * AND it should contain the error message text.
     */
    @Test
    public void handlesBusinessException() {

        // WHEN a generic Exception is thrown
        BusinessException ex = podamFactory.manufacturePojo(BusinessException.class);
        WebRequest webRequest_mock = mock(WebRequest.class);
        ResponseEntity responseEntity = restExceptionHandler_spy.handleBusinessException(ex, webRequest_mock);

        // THEN a response should be returned
        assertNotNull(responseEntity);

        // AND it should indicate an HTTP status code of 500 - Internal Server Error
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // AND it should contain the error message text.
        assertEquals(ex.getMessage(), ((Error) responseEntity.getBody()).getMessage());
    }

    /**
     * GIVEN a controller method is called
     * WHEN a generic RuntimeException is thrown
     * THEN a response should be returned
     * AND it should indicate an HTTP status code of 500 - Internal Server Error
     * AND it should contain the error message text.
     */
    @Test
    public void handlesGenericException() {

        // WHEN a generic Exception is thrown
        RuntimeException ex = podamFactory.manufacturePojo(NullPointerException.class);
        WebRequest webRequest_mock = mock(WebRequest.class);
        ResponseEntity responseEntity = restExceptionHandler_spy.handleGenericException(ex, webRequest_mock);

        // THEN a response should be returned
        assertNotNull(responseEntity);

        // AND it should indicate an HTTP status code of 500 - Internal Server Error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        // AND it should contain the error message text.
        assertEquals(ex.getMessage(), ((Error) responseEntity.getBody()).getMessage());
    }

    // -----------------------------------------------------------------------------------------------------------------
}