package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

/**
 * This repository class provides methods to return Customer data from the database.
 * <p>
 * DEVELOPER NOTE: This is the third and "deepest" layer of the application architecture. This layer is sometimes called
 * the "data access layer" (and repositories called "Data Access Objects" or DAO objects). This layer shouldn't have ANY
 * business logic in it at all. The only responsibility of a repository class is to either interface with a database to
 * retrieve and manipulate data, or to interact with an external web service. The service layer of this application
 * doesn't know or care where the repository got its data, or how it got it. It should also be "dumb" and just do its
 * job of putting and pulling data from the external source, not making business or quality decisions about the data.
 * <p>
 * This class is annotated as a @Repository, which is just an alias for the annotation @Component.Some things care
 * about the @Repository annotation, but for the most part it's just a way to be a bit more descriptive about the kind
 * of component that this class is.
 */
@Repository
class CustomerRepository {

    // ------------------------------------------------ PUBLIC METHODS -------------------------------------------------

    /**
     * Given a customer ID, return the Customer with the given ID.
     *
     * @param customerId The ID of the customer
     * @return the Customer with the given ID
     */
    Customer getCustomer(Integer customerId) {

        // Read the customer from the database
        Customer customer = readFromDatabase(customerId);

        // Post-read processing
        refreshCustomerLastReadTimestamp(customer);

        // Done
        return customer;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------- PRIVATE METHODS -------------------------------------------------

    /**
     * Given a customer ID, attempts to read the Customer record from the database and return a Customer object.
     *
     * @param customerId The ID of the customer
     * @return the Customer with the given ID
     */
    Customer readFromDatabase(Integer customerId) {

        // @TODO: Implement reading the customer from the database instead of returning this fake object
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setFullName("a fake name");

        return customer;
    }

    /**
     * Updates the given Customer's last read timestamp. DEVELOPER NOTE: There's probably no need for such a method, but
     * it is here to demonstrate the "change state" pattern of unit testing as well as how to test time-based methods.
     */
    void refreshCustomerLastReadTimestamp(Customer customer) {
        customer.setLastReadTimestamp(now());
    }

    /**
     * Returns the current time.
     */
    ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
