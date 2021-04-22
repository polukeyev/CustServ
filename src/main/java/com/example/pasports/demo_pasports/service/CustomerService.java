package com.example.pasports.demo_pasports.service;

import com.example.pasports.demo_pasports.model.Address;
import com.example.pasports.demo_pasports.model.Customer;
import com.example.pasports.demo_pasports.repository.AddressRepo;
import com.example.pasports.demo_pasports.repository.CustomerRepo;
import com.example.pasports.demo_pasports.repository.CustomerSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private CustomerSearch customerSearch;

    @Transactional
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        return customers;
    }

    @Transactional
    public List<Customer> searchByNameAndLastName(String name) {
        List<Customer> searchResults = customerSearch.search(name);
        return searchResults;
    }

    // Customer.constructor take one address and put it on both addresses (reg and current),
    // we output regAddress from Costumer and set Created/Modified fields,
    // then we save new Costumer.
    // If this address already exist, we set existing address from DB to new Costumer,
    // because people can live in same flat.
    @Transactional
    public Customer addNewCustomer(Customer customer) {
        //extracting address from input customer and check it's exist in DB
        Address address = customer.getRegistredAddress();
        address.setCreated(LocalDateTime.now());
        address.setModified(LocalDateTime.now());
        Address address1 = addressRepo.findByCountryAndRegionAndCityAndStreetAndHouseAndFlat(
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getFlat());
        if (address1 != null) {
            address = address1;
            address.setModified(LocalDateTime.now());
        }

        // save new Customer
        Customer _customer = customerRepo.save(new Customer(
                customer.getFirstName()
                , customer.getLastName()
                , customer.getMiddleName()
                , customer.getSex()
                , address));

        return _customer;
    }

    @Transactional
    public Customer changeCurrentAddress(Long id, Address address) {

        //checking customer for existing
        Optional<Customer> optional = customerRepo.findById(id);
        Customer customer;

        if (optional.isPresent()) {
            customer = optional.get();
        } else return null;

        //getting ID of exCurrent address
        Long addressId = customer.getCurrentAddress().getId();

        address.setId(Long.valueOf(0));

        // Check new address for existing
        Address address1 = addressRepo.findByCountryAndRegionAndCityAndStreetAndHouseAndFlat(
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getFlat());
        if (address1 != null) {
            address = address1;
            address.setModified(LocalDateTime.now());
        }
        customer.setCurrentAddress(address);

        // check exCurrentAddress for Customers in. If it has no Customers, delete it. Else set modified time
        Address address2 = addressRepo.findById(addressId).get();
        if (address2.getLiveCustomers().size() == 0 && address2.getRegCustomers().size() == 0)
            addressRepo.deleteById(addressId);
        else {
            address2.setModified(LocalDateTime.now());
            addressRepo.save(address2);
        }

        //save Customer to DB and return it from method
        customerRepo.save(customer);
        return customer;
    }
}
