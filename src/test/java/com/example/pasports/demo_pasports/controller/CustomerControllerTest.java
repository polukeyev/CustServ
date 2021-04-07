package com.example.pasports.demo_pasports.controller;

import com.example.pasports.demo_pasports.model.Address;
import com.example.pasports.demo_pasports.model.Customer;
import com.example.pasports.demo_pasports.repository.AddressRepo;
import com.example.pasports.demo_pasports.repository.CustomerRepo;
import com.example.pasports.demo_pasports.repository.CustomerSearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepo customerRepo;
    @MockBean
    private AddressRepo addressRepo;
    @MockBean
    private CustomerSearch customerSearch;

    private Customer customer1;
    private Customer customer2;
    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp() throws Exception {

        address1 = new Address();
        address2 = new Address();
        address1.setId(1L);
        address2.setId(2L);

        customer1 = new Customer();
        customer2 = new Customer();

        customer1.setId(1L);
        customer1.setSex("male");
        customer1.setFirstName("Ivan");
        customer1.setLastName("Ivanov");
        customer1.setRegistredAddress(address1);
        customer1.setCurrentAddress(address2);

        customer2.setId(2L);
        customer1.setSex("male");
        customer2.setFirstName("Petr");
        customer2.setLastName("Petrov");
        customer2.setRegistredAddress(address2);
        customer2.setCurrentAddress(address1);

    }

    @Test
    void showAllCustomers() throws Exception {

        when(customerRepo.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[*].id",containsInAnyOrder(1,2)))
                .andExpect(jsonPath("$[*].firstName",containsInAnyOrder("Petr","Ivan")));
    }

    @Test
    void searchByNameAndLastName() throws Exception {

        when(customerSearch.search("Petr Petrov")).thenReturn(Collections.singletonList(customer2));

        mockMvc.perform(get("/customers/search?name=Petr Petrov"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].firstName", contains("Petr")))
                .andExpect(jsonPath("$[*].lastName", contains("Petrov")));
    }

    @Test
    void addNewCustomer() throws Exception {

        when(customerRepo.save(customer1)).thenReturn(customer1);

        String json = new ObjectMapper().writeValueAsString(customer1);

        mockMvc.perform(post("/customers")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());
    }
}