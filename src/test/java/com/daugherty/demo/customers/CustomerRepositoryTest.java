package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

public class CustomerRepositoryTest { // <-- (1) JUnit instantiates a new instance of this class for each @Test

    // --------------------------- MEMBER VARIABLES ---------------------------

    public static final PodamFactory podam = new PodamFactoryImpl();

    @Spy // <- (4) <-- Mockito finds this @Spy, creates a spy instance of this class, and sets this variable
    private CustomerRepository demoRepository_spy;

    // ------------------------------------------------------------------------

    // ----------------------------- TEST METHODS -----------------------------

    @Before // (2) <-- JUnit calls the @Before method
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // <-- (3) This line is executed
    }

    /**
     * Given that a valid Customer is requested, when an attempt is made to retrieve the Customer by its ID,
     * then the customer record with the given ID should be read from the database and returned as a Customer object.
     */
    @Test // <!-- (5) JUnit calls the test method of the (per test) DemoRepository instance
    public void getCustomer() {

        // Prepare
        Customer expectedCustomer = podam.manufacturePojo(Customer.class);
        Integer customerId = expectedCustomer.getId();

        // Mock  // <-- (6) We 'override' the readFromDatabase() method so that it returns the data we want
        doReturn(expectedCustomer).when(demoRepository_spy).readFromDatabase(customerId);

        // Call // <-- (7) Calls the real getCustomer(), but not the real readFromDatabase()
        Customer actualCustomer = demoRepository_spy.getCustomer(customerId);

        // Assert
        assertEquals("The expected customer should be returned", expectedCustomer, actualCustomer);
    }

    /**
     * Given a customer with a given ID is in the database, when an attempt is made to read the Customer record from
     * the database, then the customer record with the given ID should be read from the database and returned as a Customer object.
     */
    @Test
    public void readFromDatabase() {

        // Prepare
        Integer customerId = -999;

        // Call @TODO implement the real readFromDatabase() method
        Customer customer = demoRepository_spy.readFromDatabase(customerId);

        // Assert
        assertEquals("The ID should be correct", customerId, customer.getId());
        assertEquals("The full name should be correct", "a fake name", customer.getFullName());
    }

    @Test
    public void streamTest() {

        List<Vendor> vendors = new ArrayList<>();
        vendors.add(new Vendor());
        vendors.add(new Vendor());
        vendors.add(new Vendor());

        List<Integer> locationIds = vendors.stream().map(vendor -> vendor.locations.get(0).id).collect(Collectors.toList());

        System.out.println(locationIds);

    }

    class Location {
        public Integer id;

        public Location(Integer id) {
            this.id = id;
        }
    }

    class Vendor {
        public List<Location> locations = new ArrayList<>();

        public Vendor() {
            locations.add(new Location(1));
            locations.add(new Location(2));
            locations.add(new Location(3));
        }
    }

    // ------------------------------------------------------------------------
}
