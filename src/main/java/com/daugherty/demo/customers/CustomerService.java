package com.daugherty.demo.customers;

import com.daugherty.demo.customers.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides business functionality for managing Customers.
 * <p>
 * DEVELOPER NOTE: This is the second, and middle, layer of a web application. It is frequently called the "service layer"
 * or "business logic layer". This layer is the core of the application, containing the classes that actually provide
 * business value and define the real functionality of the application. Previously, the API/Controller layer cared only
 * about receiving and returning JSON requests. This class is the real meat of the system... and theoretically shouldn't
 * know anything about the fact that it "lives" in a web application. There's nothing related to JSON, REST, HTTP, or
 * web services here.
 * <p>
 * This class is annotated as a @Service, which is just an alias for the annotation @Component. Some things care about
 * the @Service annotation, but for the most part it's just a way to be a bit more descriptive about the kind of component
 * that this class is.
 * <p>
 * Also, this layer shouldn't care from *where* it gets the data that it operates on. Service classes shouldn't
 * know anything about databases, external web services, file systems, or any other technicalities of shuttling data
 * around the system. That's the job of the final layer.
 *
 * @see com.daugherty.demo.customers.CustomerRepository
 */
@Service
class CustomerService {

    // ------------------------------------------------- DEPENDENCIES --------------------------------------------------

    /**
     * DEVELOPER NOTE: This class is dependent on the existence of a CustomerRepository object... which will created and
     * injected by Spring prior to this object's construction.
     *
     * @see com.daugherty.demo.customers.CustomerRepository
     */
    private final CustomerRepository customerRepository;

    // -----------------------------------------------------------------------------------------------------------------


    // ------------------------------------------------- CONSTRUCTORS --------------------------------------------------

    /**
     * DEVELOPER NOTE: Again we're using constructor-injection instead of field-injection. Embrace it.
     */
    @Autowired
    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------ PUBLIC METHODS -------------------------------------------------

    /**
     * Given the ID of a customer, return the Customer with that ID.
     * <p>
     * DEVELOPER NOTE: There's not much going on here, is there. *This* method doesn't have any business logic, so it's
     * just a pass-through to the CustomerRepository. There might be a temptation to have the Controller talk directly
     * to the Repository, but this is a bad idea. It's best to respect the 3-layered nature of the application
     * architecture. Inevitably, you'll have to add some real business logic, and you'll need a place to put it.
     */
    Customer getCustomer(Integer customerId) {
        return this.customerRepository.getCustomer(customerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
}