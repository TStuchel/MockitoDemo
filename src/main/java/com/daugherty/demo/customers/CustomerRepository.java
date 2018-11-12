package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import org.springframework.stereotype.Component;

/**
 * This repository class provides methods to return Customer data from the database.
 */
@Component
public class CustomerRepository {

    /**
     * Given a customer ID, return the Customer with the given ID.
     *
     * @param customerId The ID of the customer
     * @return the Customer with the given ID
     */
    public Customer getCustomer(Integer customerId) {

        // Read the customer from the database
        Customer customer = this.readFromDatabase(customerId);

        // @TODO: Implement some post-read processing if necessary

        // Done
        return customer;
    }

    /**
     * Given a customer ID, attempts to read the Customer record from the database and return a Customer object.
     *
     * @param customerId The ID of the customer
     * @return the Customer with the given ID
     */
    protected Customer readFromDatabase(Integer customerId) {

        // @TODO: Implement reading the customer from the database instead of returning this fake object
        Customer customer = new Customer();
        customer.setId(-999);
        customer.setFullName("a fake name");

        return customer;
    }
}
