package com.example.osbb.dao.owner;

import com.example.osbb.entity.owner.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface VehicleDAO extends JpaRepository<Vehicle,Long> {
    Vehicle findByNumberVehicle(String numberVehicle);
    boolean existsByNumberVehicle(String numberVehicle);
}
