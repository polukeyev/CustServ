package com.example.pasports.demo_pasports.model;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="customer")
@Indexed(index = "idx_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name="first_name")
    @Field
    private String firstName;

    @Column(name="last_name")
    @Field
    private String lastName;

    @Column(name="middle_name")
    private String middleName;

    @Column
    @NotNull
    @Pattern(regexp = "^(male)$|^(female)$")
    private String sex;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name="registred_address_id")
    @NotNull
    private Address registredAddress;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name="actual_address_id")
    @NotNull
    private Address currentAddress;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String middleName, String sex, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.sex = sex;
        this.registredAddress = address;
        this.currentAddress = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Address getRegistredAddress() {
        return registredAddress;
    }

    public void setRegistredAddress(Address registredAddress) {
        this.registredAddress = registredAddress;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }
}
