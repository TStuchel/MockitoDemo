package com.daugherty.demo.customers;

import com.daugherty.demo.BaseTest;
import com.daugherty.demo.customers.entity.Customer;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * DEVELOPER NOTE: This @RunWith(SpringRunner) is needed so that a "real" SpringBoot context will be initialized. We want
 * this so that we can make "real" REST calls. While it is technically possible to test directly against the
 * CustomerController class, it's useful to make sure that we've wired up all of those @RestController annotations
 * correctly, including properly handling HTTP status code responses, URL mappings, and content type configurations.
 */
@RunWith(SpringRunner.class)
public class CustomerControllerTest extends BaseTest {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    /**
     * DEVELOPER NOTE: This @Mock annotation is part of Mockito, but since we annotated this class with SpringRunner,
     * SpringRunner will initialize all of Mockito's annotations for us, so it will cause this variable to be set
     * prior to setup() being called.
     */
    @Mock
    private CustomerService customerService_mock;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Create a "mock" client that can call the SpringRunner-ed instance of SpringBoot. We could have used the
     * annotation @AutoConfigureMockMvc on this class and used @Autowired on this field, but due to the preferred form
     * of explicitly building and spying the tested CustomerController we want to manually wire this up in setup()
     * instead.
     */
    private MockMvc mockMvc;

    /**
     * Class under test (spied to test protected methods)
     */
    private CustomerController customerController_spy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @Before
    public void setup() {

        // Create a spy so that protected methods can/may be mocked
        customerController_spy = spy(new CustomerController(customerService_mock));

        // Set up Mock MVC for HTTP calls. DEVELOPER NOTE: This makes Spring send requests to our spied controller,
        // instead of whatever controller component it would have created and wired up itself.
        mockMvc = MockMvcBuilders.standaloneSetup(customerController_spy).build();
    }

    /**
     * GIVEN a valid customer ID and a customer with that ID is in the system
     * WHEN the GET customer API endpoint is called
     * THEN the Customer with the given ID should be returned.
     */
    @Test
    public void getCustomer_success() throws Exception {

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
        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer.getId(), actualCustomer.getId());
        assertEquals(expectedCustomer.getFullName(), actualCustomer.getFullName());

        // Verify dependency mocks
        verify(customerService_mock).getCustomer(customerId);
    }

    /**
     * GIVEN an invalid customer ID
     * WHEN the GET customer API endpoint is called
     * THEN a BAD REQUEST should be returned.
     */
    @Test
    public void getCustomer_invalidCustomerId() throws Exception {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Integer customerId = -RandomUtils.nextInt(0, 99999);

        // Dependency Mocks
        doReturn(false).when(customerController_spy).isValidCustomerId(customerId);

        // WHEN the customer API endpoint is called
        String uri = String.format("/v1/customers/%s", customerId);
        MvcResult result = this.mockMvc.perform(get(uri)).andExpect(status().isBadRequest()).andReturn();
        Error error = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Error.class);

        // THEN a INTERNAL SERVER ERROR should be returned containing an error message.
        assertEquals("Invalid customer ID [" + customerId + "]", error.getMessage());

        // Verify dependency mocks
        verify(customerController_spy).isValidCustomerId(customerId);
    }

    /**
     * GIVEN a valid customer ID and a customer with that ID is in the system
     * WHEN the GET customer API endpoint is called, but an error is thrown while retrieving the customer
     * THEN a INTERNAL SERVER ERROR should be returned containing an error message.
     */
    @Test
    public void getCustomer_internalServerError() throws Exception {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Dependency Mocks
        String expectedErrorMsg = "An error has occurred.";
        doThrow(new Error(expectedErrorMsg)).when(customerService_mock).getCustomer(customerId);

        // WHEN the customer API endpoint is called
        String uri = String.format("/v1/customers/%s", customerId);
        MvcResult result = this.mockMvc.perform(get(uri)).andExpect(status().isInternalServerError()).andReturn();
        Error error = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Error.class);

        // THEN a INTERNAL SERVER ERROR should be returned containing an error message.
        assertEquals(expectedErrorMsg, error.getMessage());

        // Verify dependency mocks
        verify(customerService_mock).getCustomer(customerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
