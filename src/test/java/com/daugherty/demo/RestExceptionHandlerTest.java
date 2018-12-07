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

    private RestExceptionHandler restExceptionHandlerSpy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @BeforeEach
    public void setup() {
        super.setup();
        restExceptionHandlerSpy = spy(new RestExceptionHandler());
    }

    /**
     * GIVEN a controller method is called
     * WHEN a BusinessException is thrown
     * THEN a response should be returned
     * AND it should indicate an HTTP status code of 400 - Bad Request
     * AND it should contain the error message text.
     */
    @Test
    void handlesBusinessException() {

        // WHEN a generic Exception is thrown
        BusinessException ex = podamFactory.manufacturePojo(BusinessException.class);
        WebRequest webRequestMock = mock(WebRequest.class);
        ResponseEntity<Object> responseEntity = restExceptionHandlerSpy.handleBusinessException(ex, webRequestMock);
        Throwable actualException = (Throwable) responseEntity.getBody();

        // THEN a response should be returned
        assertNotNull(actualException);

        // AND it should indicate an HTTP status code of 500 - Internal Server Error
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // AND it should contain the error message text.
        assertEquals(ex.getMessage(), actualException.getMessage());
    }

    /**
     * GIVEN a controller method is called
     * WHEN a generic RuntimeException is thrown
     * THEN a response should be returned
     * AND it should indicate an HTTP status code of 500 - Internal Server Error
     * AND it should contain the error message text.
     */
    @Test
    void handlesGenericException() {

        // WHEN a generic Exception is thrown
        RuntimeException expectedException = podamFactory.manufacturePojo(NullPointerException.class);
        WebRequest webRequestMock = mock(WebRequest.class);
        ResponseEntity<Object> responseEntity = restExceptionHandlerSpy.handleGenericException(expectedException, webRequestMock);
        Throwable actualException = (Throwable) responseEntity.getBody();

        // THEN a response should be returned
        assertNotNull(actualException);

        // AND it should indicate an HTTP status code of 500 - Internal Server Error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        // AND it should contain the error message text.
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    // -----------------------------------------------------------------------------------------------------------------
}