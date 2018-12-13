package com.daugherty.demo.customer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Business entity representing a Customer. This business entity is able to be persisted to a database using JPA.
 *
 * @see com.daugherty.demo.customer.contract.CustomerDTO
 */
@Getter
@Setter
@Entity
@Table(name = "CUSTOMERS")
public class Customer {

    // -------------------------------------------------- PROPERTIES ---------------------------------------------------

    @Id
    @Column(name = "CUST_ID")
    private Integer id;

    @Column(name = "CUST_STREET_ADDR")
    private String streetAddress;

    @Column(name = "CUST_FULL_NAME")
    private String fullName;

    @Transient
    private ZonedDateTime lastReadTimestamp;

    // -----------------------------------------------------------------------------------------------------------------
}


