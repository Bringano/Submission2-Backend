package se.yrgo.carservice.dto;

public record VehicleDto(
        Long id,
        String registrationNumber,
        String brand,
        String model,
        int productionYear
) { }
