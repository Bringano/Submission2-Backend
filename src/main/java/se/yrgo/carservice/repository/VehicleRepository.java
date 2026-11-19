package se.yrgo.carservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.carservice.model.Customer;
import se.yrgo.carservice.model.Vehicle;
import se.yrgo.carservice.projection.VehicleBrandProjection;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByBrand(String brand);

    @Modifying
    @Transactional
    @Query("UPDATE Vehicle v SET v.brand = :brand WHERE v.id = :id")
    int updateBrand(@Param("id") Long id, @Param("brand") String brand);

    @Query("SELECT v.registrationNumber AS registrationNumber, v.brand AS brand FROM Vehicle v")
    List<VehicleBrandProjection> findAllBrandInfo();
}