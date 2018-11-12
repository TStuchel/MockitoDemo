package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    @Mock
    private final CustomerRepository customerRepository_mock = null;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Used for random test values
     */
    public static final PodamFactory podamFactory = new PodamFactoryImpl();

    /**
     * Class under test
     */
    private CustomerService customerService_spy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService_spy = spy(new CustomerService(customerRepository_mock));
    }

    /**
     * GIVEN a valid customer ID and a customer with that ID is in the system
     * WHEN the customer is requested
     * THEN the Customer with the given ID should be returned.
     */
    @Test
    public void getCustomer() {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Mock dependencies
        doReturn(expectedCustomer).when(customerRepository_mock).getCustomer(customerId);

        // WHEN the customer is requested
        Customer actualCustomer = customerService_spy.getCustomer(customerId);

        //  THEN the Customer with the given ID should be returned.
        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer.getId(), actualCustomer.getId());
        assertEquals(expectedCustomer.getFullName(), actualCustomer.getFullName());

        // Verify dependency mocks
        verify(customerRepository_mock).getCustomer(customerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
}