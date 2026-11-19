package se.yrgo.carservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.yrgo.carservice.dto.CustomerDto;
import se.yrgo.carservice.dto.VehicleBrandDto;
import se.yrgo.carservice.dto.VehicleDto;
import se.yrgo.carservice.model.Customer;
import se.yrgo.carservice.model.Vehicle;
import se.yrgo.carservice.projection.VehicleBrandProjection;
import se.yrgo.carservice.repository.CustomerRepository;
import se.yrgo.carservice.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CarServiceController {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public CarServiceController(CustomerRepository customerRepository,
                              VehicleRepository vehicleRepository) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Customers

    @PostMapping("/customers")
    public CustomerDto createCustomer(@RequestParam String name,
                                      @RequestParam String phone) {
        Customer saved = customerRepository.save(new Customer(name, phone));
        return mapToCustomerDto(saved);
    }

    @GetMapping("/customer-id")
    public Long getCustomerId(@RequestParam String name) {
        Optional<Customer> customer = customerRepository.findByName(name);
        if (customer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No customer named: " + name);
        }
        return customer.get().getId();
    }

    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAllWithVehicles();
        List<CustomerDto> result = new ArrayList<>();
        for (Customer c : customers) {
            result.add(mapToCustomerDto(c));
        }
        return result;
    }

    // Vehicles

    @PostMapping("/vehicles")
    public VehicleDto createVehicle(@RequestParam String registrationNumber,
                                    @RequestParam String brand,
                                    @RequestParam String model,
                                    @RequestParam int productionYear) {
        Vehicle saved = vehicleRepository.save(
                new Vehicle(registrationNumber, brand, model, productionYear));
        return mapToVehicleDto(saved);
    }

    @GetMapping("/vehicles")
    public List<VehicleDto> getAllVehicles() {
        List<VehicleDto> result = new ArrayList<>();
        for (Vehicle v : vehicleRepository.findAll()) {
            result.add(mapToVehicleDto(v));
        }
        return result;
    }

    @GetMapping("/vehicles-by-brand")
    public List<VehicleDto> getVehiclesByBrand(@RequestParam String brand) {
        List<VehicleDto> result = new ArrayList<>();
        for (Vehicle v : vehicleRepository.findByBrand(brand)) {
            result.add(mapToVehicleDto(v));
        }
        return result;
    }

    @PostMapping("/addvehicletocustomer")
    public String addVehicleToCustomer(@RequestParam Long customerId,
                                       @RequestParam Long vehicleId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer existing with id: " + customerId);
        }
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No vehicle existing with id: " + vehicleId);
        }

        Vehicle v = vehicle.get();
        v.setCustomer(customer.get());
        vehicleRepository.save(v);

        return "Vehicle " + v.getRegistrationNumber() + " has customer: " + customer.get().getName();
    }

    @PutMapping("/vehicles/{id}/brand")
    public String updateBrand(@PathVariable Long id,
                              @RequestParam String brand) {
        int updated = vehicleRepository.updateBrand(id, brand);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No vehicle existing with id: " + id);
        }
        return "Vehicle " + id + " now has brand: " + brand;
    }

    @GetMapping("/vehicle-brand-info")
    public List<VehicleBrandDto> getVehicleBrandInfo() {
        List<VehicleBrandProjection> projections = vehicleRepository.findAllBrandInfo();

        List<VehicleBrandDto> result = new ArrayList<>();
        for (VehicleBrandProjection p : projections) {
            result.add(new VehicleBrandDto(p.getRegistrationNumber(), p.getBrand()));
        }
        return result;
    }

    // Mapping - helpers
    private VehicleDto mapToVehicleDto(Vehicle v) {
        return new VehicleDto(v.getId(), v.getRegistrationNumber(),
                v.getBrand(), v.getModel(), v.getProductionYear());
    }

    private CustomerDto mapToCustomerDto(Customer c) {
        List<VehicleDto> vehicleDtos = new ArrayList<>();
        for (Vehicle v : c.getVehicles()) {
            vehicleDtos.add(mapToVehicleDto(v));
        }
        return new CustomerDto(c.getId(), c.getName(), c.getPhoneNumber(), vehicleDtos);
    }


}