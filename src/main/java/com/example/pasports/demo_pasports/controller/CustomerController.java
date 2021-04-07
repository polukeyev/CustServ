package com.example.pasports.demo_pasports.controller;

import com.example.pasports.demo_pasports.model.Address;
import com.example.pasports.demo_pasports.model.Customer;
import com.example.pasports.demo_pasports.repository.AddressRepo;
import com.example.pasports.demo_pasports.repository.CustomerRepo;
import com.example.pasports.demo_pasports.repository.CustomerSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // connecting frontend
@RestController
public class CustomerController {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private CustomerSearch customerSearch;

    // get the List of Customers method
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> showAllCustomers() {
        try {
            List<Customer> customers = customerRepo.findAll();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get the List of relevant Customers by first- and lastname
    @GetMapping("/customers/search")
    public ResponseEntity<List<Customer>> searchByNameAndLastName(@RequestParam String name) {
        try {
            List<Customer> searchResults = customerSearch.search(name);
            if (searchResults.size() > 0) {
                return new ResponseEntity<>(searchResults, HttpStatus.OK);
                //if list.size=0 return not found status
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Customer.constructor take one address and put it on both addresses (reg and current),
    // we output regAddress from Costumer and set Created/Modified fields,
    // then we save new Costumer.
    // If this address already exist, we set existing address from DB to new Costumer,
    // because people can live in same flat.
    @PostMapping("/customers")
    public ResponseEntity<Customer> addNewCustomer(@RequestBody @Valid Customer customer) {
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
        try {
            Customer _customer = customerRepo.save(new Customer(
                    customer.getFirstName()
                    , customer.getLastName()
                    , customer.getMiddleName()
                    , customer.getSex()
                    , address));
            return new ResponseEntity<>(_customer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("customers/{id}")
    public ResponseEntity<Customer> changeCurrentAddress(@PathVariable Long id, @RequestBody @Valid Address address) {
        Customer customer = customerRepo.findById(id).get();
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

        // check exCurrentAddress on Customers in. If it has no Customers, delete it. Else set modified time
        Address address2 = addressRepo.findById(addressId).get();
        if (address2.getLiveCustomers().size() == 0 && address2.getRegCustomers().size() == 0)
            addressRepo.deleteById(addressId);
        else {
            address2.setModified(LocalDateTime.now());
            addressRepo.save(address2);
        }

        // save customer with new actual address
        try {
            customerRepo.save(customer);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
