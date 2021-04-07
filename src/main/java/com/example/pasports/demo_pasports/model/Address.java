package com.example.pasports.demo_pasports.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    @Size(max=255)
    private String country;

    @Column
    @Size(max=255)
    private String region;

    @Column
    @Size(max=255)
    private String city;

    @Column
    @Size(max=255)
    private String street;

    @Column
    @Size(max=255)
    private String house;

    @Column
    @Size(max=255)
    private String flat;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime modified;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
        mappedBy = "registredAddress")
    @JsonIgnore
    private List<Customer> regCustomers;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
        mappedBy = "currentAddress")
    @JsonIgnore
    private List<Customer> liveCustomers;

    public Address() {
    }

    public Address(String country, String region, String city, String street, String house, String flat) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public List<Customer> getRegCustomers() {
        return regCustomers;
    }

    public void setRegCustomers(List<Customer> regCustomers) {
        this.regCustomers = regCustomers;
    }

    public List<Customer> getLiveCustomers() {
        return liveCustomers;
    }

    public void setLiveCustomers(List<Customer> liveCustomers) {
        this.liveCustomers = liveCustomers;
    }
}
