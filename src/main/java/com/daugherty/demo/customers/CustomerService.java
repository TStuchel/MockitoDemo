package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerService {


    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    @Autowired
    private final CustomerRepository customerRepository;

    // -----------------------------------------------------------------------------------------------------------------


    // ------------------------------------------------- CONSTRUCTORS --------------------------------------------------

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------ PUBLIC METHODS -------------------------------------------------

    /**
     * Given the ID of a customer, return the Customer with that ID.
     *
     * @param customerId
     * @return
     */
    public Customer getCustomer(Integer customerId) {
        return this.customerRepository.getCustomer(customerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
