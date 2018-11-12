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
 * <p>
 * DEVELOPER NOTE: The first layer of a web application is the "API" layer. In this case, this layer is implemented
 * using a REST-ful endpoint that exposes a way to retrieve a Customer object via the Customer's ID.
 * <p>
 * The @RestController annotation tells Spring that this class is a "controller" that can handle incoming HTTP requests.
 *
 * @see com.daugherty.demo.customers.CustomerService next!
 */
@RestController
public class CustomerController {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    /**
     * DEVELOPER NOTE: This class has a dependency (requires... needs it to work... can't live without it) a class from
     * the next "service" layer of the application called "CustomerService". It's pretty common to have a suffixed naming
     * convention for classes in each layer. Note that this variable is both private and "final". Once the variable is
     * set (in the constructor), it will never be changed again.
     */
    private final CustomerService customerService;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- CONSTRUCTORS --------------------------------------------------

    /**
     * DEVELOPER NOTE: Back when Spring was launched...
     *
     * @see com.daugherty.demo.Application
     * <p>
     * ... Spring searched the classpath to find all of the annotated classes. Since this constructor has @Autowired on
     * it, Spring knows it can't create an instance of this class until it first creates an instance of the
     * CustomerService. Doing dependency injection this way ends up being better than "field injection" where the
     * annotation @Autowired would be on the customerService member vairable. There are quite a few real and stylistic
     * reasons for this, but it's best to get used to this form intead of autowiring member variables. Dependency
     * injection this way forces Spring to build classes in a true dependency-heirarchical order. Field injection
     * lets Spring build objects in any order, and this can result in weird behavior if you try to access a dependency
     * before Spring creates it.
     */
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------ PUBLIC METHODS -------------------------------------------------

    /**
     * Given a customer ID, return a Customer with the given ID.
     * <p>
     * DEVELOPER NOTE: This method is annotated such that any GET requests to the /v1/customers/{customerId} URL will be
     * fed into this method. Notice that is method returns ResponseEntity<?>. This is so the method can return more than
     * one type of object... in this case either a Customer or an Error.
     * <p>
     * It's good form to always know what HTTP status code your controllers will return and under what conditions. It's
     * bad form to just return 200 for "good" and 500 for "bad". There's a rich collection of choices to return specific
     * HTTP responses depending on the result of the call. See: https://www.restapitutorial.com/httpstatuscodes.html
     */
    @RequestMapping(method = RequestMethod.GET, path = "/v1/customers/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomer(@PathVariable("customerId") Integer customerId) {

        // Verify contract
        if (!this.isValidCustomerId(customerId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("Invalid customer ID [" + customerId + "]"));
        }

        try {

            // DEVELOPER NOTE: Keep in mind that every line of code might blow up with an Exception. It's good form
            // to let exceptions bubble up to the controller, where it can decide on what HTTP response to return.
            Customer customer = customerService.getCustomer(customerId);

            // Return OK and the Customer
            return ResponseEntity.ok().body(customer);

        }
        // DEVELOPER NOTE: Another option in this case is to use Spring's ResponseEntityExceptionHandler framework.
        // See https://www.baeldung.com/exception-handling-for-rest-with-spring
        catch (Error ex) {

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
