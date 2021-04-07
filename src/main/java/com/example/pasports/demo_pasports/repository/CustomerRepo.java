package com.example.pasports.demo_pasports.repository;

import com.example.pasports.demo_pasports.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
