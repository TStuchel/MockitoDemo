package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This REST API controller is responsible for providing an API for managing Customer entities.
 */
@RestController
public class CustomerController {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    @Autowired
    private final CustomerService customerService;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- CONSTRUCTORS --------------------------------------------------

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------ PUBLIC METHODS -------------------------------------------------

    /**
     * Given a customer ID, return a Customer with the given ID.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/v1/customers/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomer(@PathVariable("customerId") Integer customerId) {

        // Verify contract
        if (!this.isValidCustomerId(customerId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("Invalid customer ID [" + customerId + "]"));
        }

        // Call service
        try {

            // Return OK
            Customer customer = customerService.getCustomer(customerId);
            return ResponseEntity.ok().body(customer);

        } catch (Error ex) {

            // Respond INTERNAL_SERVER_ERROR
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------- PRIVATE METHODS -------------------------------------------------

    /**
     * Returns whether or not the given customer ID is valid.
     *
     * @param customerId
     * @return
     */
    protected boolean isValidCustomerId(Integer customerId) {
        return customerId != null && customerId > 0;
    }

    // -----------------------------------------------------------------------------------------------------------------
}
