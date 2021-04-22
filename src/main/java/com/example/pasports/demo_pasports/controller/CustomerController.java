package com.example.pasports.demo_pasports.controller;

import com.example.pasports.demo_pasports.model.Address;
import com.example.pasports.demo_pasports.model.Customer;
import com.example.pasports.demo_pasports.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // connecting frontend
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // getting the List of Customers method
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // getting the List of relevant Customers by first- and lastname
    @GetMapping("/customers/search")
    public ResponseEntity<List<Customer>> searchByNameAndLastName(@RequestParam String name) {
        try {
            List<Customer> searchResults = customerService.searchByNameAndLastName(name);
            if (searchResults.size() > 0)
                return new ResponseEntity<>(searchResults, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); //if list.size=0 return not found status
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // creation of new customer
    @PostMapping("/customers")
    public ResponseEntity<Customer> addNewCustomer(@RequestBody @Valid Customer customer) {
        try {
            Customer _customer = customerService.addNewCustomer(customer);
            return new ResponseEntity<>(_customer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("customers/{id}")
    public ResponseEntity<Customer> changeCurrentAddress(@PathVariable Long id, @RequestBody @Valid Address address) {
        // save customer with new actual address
        try {
            Customer customer = customerService.changeCurrentAddress(id, address);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
