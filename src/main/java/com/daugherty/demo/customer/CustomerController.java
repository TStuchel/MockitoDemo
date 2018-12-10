package com.daugherty.demo.customer;

import com.daugherty.demo.customer.contract.CustomerDTO;
import com.daugherty.demo.customer.entity.Customer;
import com.daugherty.demo.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * This REST API controller is responsible for providing an API for managing Customer entities.
 * <p>
 * DEVELOPER NOTE: The first layer of a web application is the "API" layer. In this case, this layer is implemented
 * using a REST-ful endpoint that exposes a way to retrieve a Customer object via the Customer's ID.
 * <p>
 * The @RestController annotation tells Spring that this class is a "controller" that can handle incoming HTTP
 * requests.
 *
 * @see com.daugherty.demo.customer.CustomerService next!
 */
@RestController
public class CustomerController {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    /**
     * DEVELOPER NOTE: This class has dependencies (requires... needs it to work... can't live without it) with classes from
     * the next "service" layer of the application called "CustomerService". It's pretty common to have a suffixed
     * naming convention for classes in each layer. Note that this variable is both private and "final". Once the
     * variable is set (in the constructor), it will never be changed again.
     */
    private final CustomerTranslator customerTranslator;
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
     * injection this way forces Spring to build classes in a true dependency-heirarchical order. Field injection lets
     * Spring build objects in any order, and this can result in weird behavior if you try to access a dependency before
     * Spring creates it.
     */
    @Autowired
    CustomerController(CustomerTranslator customerTranslator, CustomerService customerService) {
        this.customerTranslator = customerTranslator;
        this.customerService = customerService;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------ PUBLIC METHODS -------------------------------------------------

    /**
     * Given a customer ID, return a Customer with the given ID.
     * <p>
     * DEVELOPER NOTE: This method is annotated such that any GET requests to the /v1/customer/{customerId} URL will be
     * fed into this method. Notice that is method returns ResponseEntity<?>. This is so the method can return more than
     * one type of object... in this case either a Customer or an Error.
     * </p>
     *
     * @see com.daugherty.demo.RestExceptionHandler
     */
    @GetMapping(path = "/v1/customers/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") final Integer customerId) throws BusinessException {

        // DEVELOPER NOTE: Keep in mind that every line of code might blow up with an Exception. It's good form
        // to let exceptions bubble up to the controller, where it can decide on what HTTP response to return.
        final Customer customer = customerService.getCustomer(customerId);

        // Translate to contract
        CustomerDTO customerDto = customerTranslator.toContract(customer);

        // Return 200-OK and the Customer
        return ok().body(customerDto);
    }

    // -----------------------------------------------------------------------------------------------------------------

}
