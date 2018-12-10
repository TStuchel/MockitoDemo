package com.daugherty.demo.customer;

import com.daugherty.demo.BaseTest;
import com.daugherty.demo.customer.entity.Customer;
import com.daugherty.demo.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


/**
 * DEVELOPER NOTE:  Note that we don't need Spring... nothing in this test, or the tested class, cares about Spring.
 * This is all basic Mockito and JUnit. Don't involve Spring unless you have to; it just slows down your tests.
 */
class CustomerServiceTest extends BaseTest {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    @Mock
    private CustomerRepository customerRepositoryMock;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Class under test (spied to test protected methods)
     */
    private CustomerService customerServiceSpy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @BeforeEach
    public void setup() {
        super.setup();

        // Create a spy so that protected methods can/may be mocked
        customerServiceSpy = spy(new CustomerService(customerRepositoryMock));
    }

    /**
     * GIVEN a valid customer ID and a customer with that ID is in the system
     * WHEN the customer is requested
     * THEN the Customer with the given ID should be returned.
     */
    @Test
    void getCustomer() throws BusinessException {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Mock dependencies
        doReturn(expectedCustomer).when(customerRepositoryMock).getCustomer(customerId);

        // WHEN the customer is requested
        Customer actualCustomer = customerServiceSpy.getCustomer(customerId);

        //  THEN the Customer with the given ID should be returned.
        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer.getId(), actualCustomer.getId());
        assertEquals(expectedCustomer.getFullName(), actualCustomer.getFullName());

        // Verify dependency mocks
        verify(customerRepositoryMock).getCustomer(customerId);
    }

    /**
     * GIVEN a customer ID
     * WHEN the customer ID is checked to see if it is valid
     * THEN false should be returned if the customer ID is null
     * AND false should be returned if the customer ID is a negative number
     * AND true should be returned if the customer ID is a positive number
     * <p>
     * DEVELOPER NOTE: Notice the use of @ParameterizedTest and @CsvSource here. This will cause JUnit to call this
     * method once for each of the (comma-delimited) strings in the given array.
     */
    @ParameterizedTest
    @CsvSource({", false", "-999, false", "0, false", "1234, true"})
    void validCustomerId(Integer customerId, boolean expected) {
        assertEquals(expected, customerServiceSpy.isValidCustomerId(customerId));
    }

    // -----------------------------------------------------------------------------------------------------------------
}