package com.daugherty.demo.customer;

import com.daugherty.demo.BaseTest;
import com.daugherty.demo.customer.entity.Customer;
import com.daugherty.demo.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


/**
 * DEVELOPER NOTE:  Note that we don't need Spring... nothing in this test, or the tested class, cares about Spring.
 * This is all basic Mockito and JUnit. Don't involve Spring unless you have to; it just slows down your tests.
 * <p>
 * Comments have been added to this class to indicate the order that JUnit and Mockito execute this class.
 */
@ExtendWith(MockitoExtension.class) // <-- (1) JUnit will run all unit tests wrapped such that Mockito can inject mocks.
class CustomerServiceTest extends BaseTest { // <-- (2) JUnit instantiates a new instance of this class for each @Test

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    @Mock // <-- (3) Mockito sees this annotation and will create a Mock instance of this class
    private CustomerRepository customerRepositoryMock;


    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Class under test (spied to test protected methods)
     */
    @Spy // <-- (5) Mockito sees this annotation and will create a Spy instance of this class
    @InjectMocks // <-- (4) Mockito will inject any mocks into this class's constructor when it is created.
    private CustomerService customerServiceSpy;


    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @BeforeEach // (6) <-- JUnit calls the @BeforeEach method
    public void beforeEach() {
        super.setup();
    }

    /**
     * GIVEN a valid customer ID and a customer with that ID is in the system WHEN the customer is requested THEN the
     * Customer with the given ID should be returned.
     */
    @Test // <-- (7) JUnit calls this test method
    void getCustomer_found() throws BusinessException {

        // GIVEN a valid customer ID and a customer with that ID is in the system
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getCustomerId();

        // Mock dependencies
        doReturn(Optional.of(expectedCustomer)).when(customerRepositoryMock).findById(customerId);

        // WHEN the customer is requested
        Customer actualCustomer = customerServiceSpy.getCustomer(customerId);

        // THEN the Customer with the given ID should be returned.
        assertEquals(expectedCustomer, actualCustomer);

        // Verify dependency mocks
        verify(customerRepositoryMock).findById(customerId);
    }

    /**
     * GIVEN a customer ID WHEN the customer is requested, but the customer is NOT in the system THEN null should be
     * returned.
     */
    @Test
    void getCustomer_notFound() throws BusinessException {

        // GIVEN a customer ID
        Integer customerId = podamFactory.manufacturePojo(Integer.class);

        // Mock dependencies
        doReturn(Optional.empty()).when(customerRepositoryMock).findById(customerId);

        // WHEN the customer is requested, but the customer is NOT in the system
        Customer actualCustomer = customerServiceSpy.getCustomer(customerId);

        // THEN null should be returned.
        assertNull(actualCustomer);

        // Verify dependency mocks
        verify(customerRepositoryMock).findById(customerId);
    }

    /**
     * GIVEN a customer ID WHEN the customer ID is checked to see if it is valid THEN false should be returned if the
     * customer ID is null AND false should be returned if the customer ID is a negative number AND true should be
     * returned if the customer ID is a positive number
     * <p>
     * DEVELOPER NOTE: Notice the use of @ParameterizedTest and @CsvSource here. This will cause JUnit to call this
     * method once for each of the (comma-delimited) strings in the given array.
     */
    @ParameterizedTest
    @CsvSource({", false", "-999, false", "0, false", "1234, true"})
    void validCustomerId(Integer customerId, boolean expected) {
        assertEquals(expected, customerServiceSpy.isValidCustomerId(customerId));
    }

}