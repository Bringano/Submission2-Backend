package se.yrgo.carservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.yrgo.carservice.model.Customer;
import se.yrgo.carservice.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByName(String name);

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.vehicles")
    List<Customer> findAllWithVehicles();

    @Query("SELECT v FROM Vehicle v WHERE v.customer.id = :customerId")
    List<Vehicle> findVehiclesByCustomerId(@Param("customerId") Long customerId);
}
