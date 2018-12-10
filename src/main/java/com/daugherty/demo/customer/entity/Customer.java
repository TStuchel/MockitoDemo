package com.daugherty.demo.customer.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Business entity representing a Customer.
 *
 * @see com.daugherty.demo.customer.contract.CustomerDTO
 */
@Getter
@Setter
public class Customer {

    // -------------------------------------------------- PROPERTIES ---------------------------------------------------

    private Integer id;
    private String streetAddress;
    private String fullName;
    private ZonedDateTime lastReadTimestamp;

    // -----------------------------------------------------------------------------------------------------------------
}
