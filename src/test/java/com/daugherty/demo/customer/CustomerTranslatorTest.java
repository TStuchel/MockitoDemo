package com.daugherty.demo.customer;

import com.daugherty.demo.BaseTest;
import com.daugherty.demo.customer.contract.CustomerDTO;
import com.daugherty.demo.customer.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

public class CustomerTranslatorTest extends BaseTest {

    // ----------------------------------------------- MEMBER VARIABLES ------------------------------------------------

    /**
     * Class under test (spied to test protected methods)
     */
    private CustomerTranslator customerTranslatorSpy;

    // -----------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------- TEST METHODS --------------------------------------------------

    @BeforeEach
    public void setup() {
        super.setup();

        // Create a spy so that protected methods can/may be mocked
        customerTranslatorSpy = spy(new CustomerTranslator());
    }

    /**
     * GIVEN an empty CustomerDTO
     * WHEN the CustomerDTO is translated to a Customer entity
     * THEN a Customer entity should be returned
     * AND all fields of the CustomerDTO should be correctly mapped.
     * <p>
     * DEVELOPER NOTE: There should be a test that verifies a contract can be mapped where all of the fields haven't
     * been set. This/protects against null pointer exceptions and verifies default values given to optional fields.
     */
    @Test
    void toEntity_emptyTranslation() {

        // GIVEN an empty CustomerDTO
        CustomerDTO customerDto = new CustomerDTO();

        // WHEN the CustomerDTO is translated to a Customer entity
        Customer customer = customerTranslatorSpy.toEntity(customerDto);

        // THEN a Customer entity should be returned
        assertNotNull(customer);

        // AND all fields of the CustomerDTO should be correctly mapped.
        assertNull(customer.getFullName());
        assertNull(customer.getId());
        assertNull(customer.getLastReadTimestamp());
        assertEquals(customer.getStreetAddress(), "Unknown");
    }

    /**
     * GIVEN a fully populated CustomerDTO
     * WHEN the CustomerDTO is translated to a Customer entity
     * THEN a Customer entity should be returned
     * AND all fields of the CustomerDTO should be correctly mapped.
     * <p>
     * DEVELOPER NOTE: There should be a test that verifies that EVERY field is mapped.
     */
    @Test
    void toEntity_fullTranslation() {

        // GIVEN a fully populated CustomerDTO
        CustomerDTO customerDto = podamFactory.manufacturePojo(CustomerDTO.class);

        // WHEN the CustomerDTO is translated to a Customer entity
        Customer customer = customerTranslatorSpy.toEntity(customerDto);

        // THEN a Customer entity should be returned
        assertNotNull(customer);

        // AND all fields of the CustomerDTO should be correctly mapped.
        assertEquals(customerDto.getFullName(), customer.getFullName());
        assertEquals(customerDto.getId(), customer.getId());
        assertEquals(customerDto.getLastReadTimestamp(), customer.getLastReadTimestamp());
        assertEquals(customer.getStreetAddress(), "Unknown");

    }

    /**
     * GIVEN an empty Customer
     * WHEN the Customer is translated to a CustomerDTO contract
     * THEN a CustomerDTO contract should be returned
     * AND all fields of the Customer should be correctly mapped.
     * <p>
     * DEVELOPER NOTE: There should be a test that verifies an entity can be mapped where all of the fields haven't
     * been set. This/protects against null pointer exceptions and verifies default values given to optional fields.
     */
    @Test
    void toContract_emptyTranslation() {

        // GIVEN an empty Customer
        Customer customer = new Customer();

        // WHEN the Customer is translated to a CustomerDTO contract
        CustomerDTO customerDto = customerTranslatorSpy.toContract(customer);

        // THEN a CustomerDTO contract should be returned
        assertNotNull(customerDto);

        // AND all fields of the CustomerDTO should be correctly mapped.
        assertNull(customerDto.getFullName());
        assertNull(customerDto.getId());
        assertNull(customerDto.getLastReadTimestamp());
    }

    /**
     * GIVEN a fully populated Customer
     * WHEN the Customer is translated to a CustomerDTO contract
     * THEN a CustomerDTO contract should be returned
     * AND all fields of the CustomerDTO should be correctly mapped.
     * <p>
     * DEVELOPER NOTE: There should be a test that verifies that EVERY field is mapped.
     */
    @Test
    void toContract_fullTranslation() {

        // GIVEN a fully populated Customer
        Customer customer = podamFactory.manufacturePojo(Customer.class);

        // WHEN the Customer is translated to a CustomerDTO contract
        CustomerDTO customerDto = customerTranslatorSpy.toContract(customer);

        // THEN a CustomerDTO contract should be returned
        assertNotNull(customerDto);

        // AND all fields of the CustomerDTO should be correctly mapped.
        assertEquals(customer.getFullName(), customerDto.getFullName());
        assertEquals(customer.getId(), customerDto.getId());
        assertEquals(customer.getLastReadTimestamp(), customerDto.getLastReadTimestamp());
    }

    // -----------------------------------------------------------------------------------------------------------------
}
