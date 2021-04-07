package com.example.pasports.demo_pasports.repository;

import com.example.pasports.demo_pasports.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo  extends JpaRepository<Address, Long> {
    Address findByCountryAndRegionAndCityAndStreetAndHouseAndFlat(
            String country, String region, String city, String street, String house, String flat);
}
