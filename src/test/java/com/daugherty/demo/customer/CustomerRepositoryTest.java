package com.daugherty.demo.customer;

import com.daugherty.demo.BaseTest;
import com.daugherty.demo.customer.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * DEVELOPER NOTE:  Note that we don't need Spring... nothing in this test, or the tested class, cares about Spring.
 * This is all basic Mockito and JUnit. Don't involve Spring unless you have to; it just slows down your tests.
 * <p>
 * Comments have been added to this class to indicate the order that JUnit and Mockito execute this class.
 */
class CustomerRepositoryTest extends BaseTest { // <-- (1) JUnit instantiates a new instance of this class for each @Test

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Class under test (spied to test protected methods)
     */
    private CustomerRepository customerRepositorySpy;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------- TEST METHODS -----------------------------

    @BeforeEach
    // (2) <-- JUnit calls the @BeforeEach method
    public void setup() {
        super.setup(); // <-- (3) This line is executed to have Mockito scan this class for Mockito annotations
        customerRepositorySpy = spy(new CustomerRepository()); // <-- (4) A spy is created to watch calls to this object
    }

    /**
     * GIVEN a Customer with a given ID is in the database
     * WHEN an attempt is made to read the Customer record from the database
     * THEN the customer record with the given ID should be read from the database and returned as a Customer object.
     */
    @Test
    // <-- (5) JUnit calls the test method of the (per test) DemoRepository instance
    void getCustomer() {

        // Prepare
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Mock  // <-- (6) We 'override' the readFromDatabase() method so that it returns the data we want
        doReturn(expectedCustomer).when(customerRepositorySpy).readFromDatabase(customerId);

        // Call // <-- (7) Calls the real getCustomer(), but not the real readFromDatabase()
        Customer actualCustomer = customerRepositorySpy.getCustomer(customerId);

        // Assert
        assertEquals(expectedCustomer, actualCustomer, "The expected customer should be returned");
    }

    /**
     * GIVEN a Customer with a given ID is in the database
     * WHEN an attempt is made to read the Customer record from the database
     * THEN the customer record with the given ID should be read from the database and returned as a Customer object.
     */
    @Test
    void readFromDatabase() {

        //  GIVEN a Customer with a given ID is in the database
        Integer customerId = -podamFactory.manufacturePojo(Integer.class);

        // WHEN an attempt is made to read the Customer record from the database
        // @TODO implement the real readFromDatabase() method
        Customer customer = customerRepositorySpy.readFromDatabase(customerId);

        // THEN the customer record with the given ID should be read from the database and returned as a Customer object.
        assertEquals(customerId, customer.getId(), "The ID should be correct");
        assertEquals("a fake name", customer.getFullName(), "The full name should be correct");
    }

    /**
     * GIVEN a customer record
     * WHEN the customer's needs to be refreshed to indicate the last time that it was read from
     * the database
     * THEN the customer record's "last read" timestamp should be updated to "now".
     */
    @Test
    void refreshCustomerLastReadTimestamp() {

        // GIVEN a customer record
        Customer customer = podamFactory.manufacturePojo(Customer.class);
        ZonedDateTime expectedTimestamp = podamFactory.manufacturePojo(ZonedDateTime.class);

        // Mock
        doReturn(expectedTimestamp).when(customerRepositorySpy).now();

        // WHEN the customer's "last read" timestamp needs to be updated
        customerRepositorySpy.refreshCustomerLastReadTimestamp(customer);

        // THEN the customer record should have a timestamp indicating the last time that it was read from the database (now).
        assertEquals(expectedTimestamp, customer.getLastReadTimestamp());
    }

    // ------------------------------------------------------------------------
}
