package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class CustomerControllerTest {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    @MockBean
    private final CustomerService customerService_mock = null;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Used for random test values
     */
    public static final PodamFactory podamFactory = new PodamFactoryImpl();

    /**
     * Serialization/Deserialization
     */
    public static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Mock SpringBoot
     */
    private MockMvc mockMvc;

    /**
     * Class under test
     */
    private CustomerController customerController_spy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    /**
     * Wire dependencies.
     */
    @Before
    public void setup() {

        // Create a spy so that protected methods can/may be mocked
        customerController_spy = spy(new CustomerController(customerService_mock));

        // Set up Mock MVC for HTTP calls
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

        // Dependency Mocks
        doReturn(expectedCustomer).when(customerService_mock).getCustomer(customerId);

        // WHEN the customer API endpoint is called
        String uri = String.format("/v1/customers/%s", customerId);
        MvcResult result = this.mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();
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
