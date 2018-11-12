package com.daugherty.demo.customers;

import com.daugherty.demo.BaseTest;
import com.daugherty.demo.customers.entity.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

/**
 * DEVELOPER NOTE:  Note that we don't need Spring... nothing in this test, or the tested class, cares about Spring. This is all
 * basic Mockito and JUnit. Don't involve Spring unless you have to; it just slows down your tests.
 * <p>
 * Comments have been added to this class to indicate the order that JUnit and Mockito execute this class.
 */
public class CustomerRepositoryTest extends BaseTest { // <-- (1) JUnit instantiates a new instance of this class for each @Test

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Class under test (spied to test protected methods)
     */
    @Spy // <- (4) <-- Mockito finds this @Spy, creates a spy instance of this class, and sets this variable
    private CustomerRepository demoRepository_spy;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------- TEST METHODS -----------------------------

    @Before // (2) <-- JUnit calls the @Before method
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // <-- (3) This line is executed to have Mockito scan this class for Mockito annotations
    }

    /**
     * GIVEN a Customer with a given ID is in the database
     * WHEN an attempt is made to read the Customer record from the database
     * THEN the customer record with the given ID should be read from the database and returned as a Customer object.
     */
    @Test // <!-- (5) JUnit calls the test method of the (per test) DemoRepository instance
    public void getCustomer() {

        // Prepare
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Mock  // <-- (6) We 'override' the readFromDatabase() method so that it returns the data we want
        doReturn(expectedCustomer).when(demoRepository_spy).readFromDatabase(customerId);

        // Call // <-- (7) Calls the real getCustomer(), but not the real readFromDatabase()
        Customer actualCustomer = demoRepository_spy.getCustomer(customerId);

        // Assert
        assertEquals("The expected customer should be returned", expectedCustomer, actualCustomer);
    }

    /**
     * GIVEN a Customer with a given ID is in the database
     * WHEN an attempt is made to read the Customer record from the database
     * THEN the customer record with the given ID should be read from the database and returned as a Customer object.
     */
    @Test
    public void readFromDatabase() {

        //  GIVEN a Customer with a given ID is in the database
        Integer customerId = -999;

        // WHEN an attempt is made to read the Customer record from the database
        // @TODO implement the real readFromDatabase() method
        Customer customer = demoRepository_spy.readFromDatabase(customerId);

        // THEN the customer record with the given ID should be read from the database and returned as a Customer object.
        assertEquals("The ID should be correct", customerId, customer.getId());
        assertEquals("The full name should be correct", "a fake name", customer.getFullName());
    }

    // ------------------------------------------------------------------------
}
