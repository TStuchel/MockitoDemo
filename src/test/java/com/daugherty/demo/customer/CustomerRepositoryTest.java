package com.daugherty.demo.customer;

import com.daugherty.demo.BaseTest;
import com.daugherty.demo.customer.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DEVELOPER NOTES: This test class uses the @DataJpaTest annotation to test the JPA-based CustomerRepository. Unlike
 * the Controller and Service test, this test needs Spring to initialize both a JPA EntityManager (to talk to the
 * database) as well as automatically implement the CustomerRepository interface. For this reason, this test class uses
 * field-style dependency injection and does not use Mockito.
 */
@DataJpaTest
class CustomerRepositoryTest extends BaseTest {

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    @Autowired
    private TestEntityManager entityManager;

    /**
     * Class under test (spied to test protected methods)
     */
    @Autowired
    private CustomerRepository customerRepository;

    // -----------------------------------------------------------------------------------------------------------------

    // ----------------------------- TEST METHODS -----------------------------

    /**
     * GIVEN a Customer with a given ID is in the database
     * WHEN an attempt is made to read the Customer record from the database
     * THEN the customer record with the given ID should be read from the database and returned as a Customer object.
     */
    @Test
    void getCustomer() {

        // GIVEN a Customer with a given ID is in the database
        Customer expectedCustomer = podamFactory.manufacturePojo(Customer.class);
        entityManager.persistAndGetId(expectedCustomer);
        entityManager.getEntityManager().getTransaction().commit();

        // WHEN an attempt is made to read the Customer record from the database
        Customer actualCustomer = customerRepository.getOne(expectedCustomer.getId());

        // THEN the customer record with the given ID should be read from the database and returned as a Customer object.
        assertEquals(expectedCustomer, actualCustomer);
    }

    // ------------------------------------------------------------------------
}
