package com.daugherty.demo.customers;

import com.daugherty.demo.Application;
import com.daugherty.demo.BaseTest;
import com.daugherty.demo.RestExceptionHandler;
import com.daugherty.demo.customers.entity.Customer;
import com.daugherty.demo.exception.BusinessException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * DEVELOPER NOTE: The @ExtendWith(SpringExtension.class) and @ContextConfiguration(classes = {Application.class}) is
 * needed so that a "real" SpringBoot context will be initialized. We want this so that we can make "real" REST calls.
 * While it is technically possible to test directly against the CustomerController class, it's useful to make sure that
 * we've wired up all of those @RestController annotations correctly, including properly handling HTTP status code
 * responses, URL mappings, and content type configurations.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class, RestExceptionHandler.class})
class CustomerControllerTest extends BaseTest {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    /**
     * DEVELOPER NOTE: This @Mock annotation is part of Mockito, and will be re-initialized for every @Test method via
     * the @BeforeEach method.
     */
    @Mock
    private CustomerService customerService_mock;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Create a "mock" client that can call the SpringExtend-ed instance of SpringBoot.
     * DEVELOPER NOTE: We could have used the annotation @AutoConfigureMockMvc on this class and used @Autowired on this
     * field, but due to the preferred form of explicitly building and spying the tested CustomerController we want to
     * manually wire this up in setup() instead.
     */
    private MockMvc mockMvc;

    /**
     * Class under test (spied to test protected methods)
     */
    private CustomerController customerController_spy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @BeforeEach
    void setup() {

        // Initialize Mockito mocked dependencies
        MockitoAnnotations.initMocks(this);

        // Create a spy so that protected methods can/may be mocked
        customerController_spy = spy(new CustomerController(customerService_mock));

        // Set up Mock MVC for HTTP calls. DEVELOPER NOTE: This makes Spring send requests to our spied controller,
        // instead of whatever controller component it would have created and wired up itself.
        mockMvc = MockMvcBuilders.standaloneSetup(customerController_spy).setControllerAdvice(new RestExceptionHandler()).build();
    }

    /**
     * GIVEN a valid customer ID and a customer with that ID is in the system
     * WHEN the GET customer API endpoint is called
     * THEN the Customer with the given ID should be returned.
     */
    @Test
    void getCustomer_success() throws Exception {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Dependency Mocks (note that is mock only exists in the spied controller that was initialized with MockMvc)
        doReturn(expectedCustomer).when(customerService_mock).getCustomer(customerId);

        // WHEN the customer API endpoint is called (this is both a GET call and an assertion that OK is returned)
        String uri = String.format("/v1/customers/%s", customerId);
        MvcResult result = this.mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();

        // We have to deserialize because JSON was returned, not an object, proving it was a "real" REST call.
        Customer actualCustomer = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Customer.class);

        // THEN the Customer with the given ID should be returned.
        assertEquals(expectedCustomer.getId(), actualCustomer.getId());
        assertEquals(expectedCustomer.getFullName(), actualCustomer.getFullName());
        assertTrue(expectedCustomer.getLastReadTimestamp().isEqual(actualCustomer.getLastReadTimestamp()));

        // Verify dependency mocks
        verify(customerService_mock, times(1)).getCustomer(customerId);
    }

    /**
     * GIVEN an invalid customer ID
     * WHEN the GET customer API endpoint is called and a business exception is thrown
     * THEN a BAD REQUEST should be returned containing the error message.
     */
    @Test
    void getCustomer_businessException() throws Exception {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Integer customerId = -RandomUtils.nextInt(0, 99999);
        String message = "Invalid customer ID [" + customerId + "]";

        // Dependency Mocks
        doThrow(new BusinessException(message)).when(customerService_mock).getCustomer(customerId);

        // WHEN the customer API endpoint is called
        String uri = String.format("/v1/customers/%s", customerId);
        MvcResult result = this.mockMvc.perform(get(uri)).andExpect(status().isBadRequest()).andReturn();
        Error error = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Error.class);

        // THEN a BAD REQUEST should be returned containing an error message.
        assertEquals(message, error.getMessage());

        // Verify dependency mocks
        verify(customerController_spy).getCustomer(customerId);
    }

    /**
     * GIVEN a valid customer ID and a customer with that ID is in the system
     * WHEN the GET customer API endpoint is called, but an unexpected exception is thrown while retrieving the customer
     * THEN a INTERNAL SERVER ERROR should be returned containing an error message.
     */
    @Test
    void getCustomer_internalServerError() throws Exception {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Dependency Mocks
        NullPointerException expectedException = podamFactory.manufacturePojo(NullPointerException.class);
        doThrow(expectedException).when(customerService_mock).getCustomer(customerId);

        // WHEN the customer API endpoint is called
        String uri = String.format("/v1/customers/%s", customerId);
        MvcResult result = this.mockMvc.perform(get(uri)).andExpect(status().isInternalServerError()).andReturn();
        Error error = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Error.class);

        // THEN a INTERNAL SERVER ERROR should be returned containing an error message.
        assertEquals(expectedException.getMessage(), error.getMessage());

        // Verify dependency mocks
        verify(customerService_mock).getCustomer(customerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
