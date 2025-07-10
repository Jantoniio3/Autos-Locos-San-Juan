package com.autoslocos.autoslocos.Service;

import com.autoslocos.autoslocos.Entity.Vehicle;
import com.autoslocos.autoslocos.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
     @Autowired
    private VehicleRepository vehicleRepository;



    // Obtener todos los vehículos
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Añadir un nuevo vehículo con foto obligatoria (versión básica)
    public Vehicle addVehicle(String name, String driver, String contactNumber, MultipartFile photoFile) throws IOException {
        if (photoFile == null || photoFile.isEmpty()) {
            throw new IllegalArgumentException("La foto del vehículo es obligatoria");
        }
        
        Vehicle vehicle = new Vehicle(name, driver, contactNumber);
        vehicle.setPhotoFromFile(photoFile);
        return vehicleRepository.save(vehicle);
    }

    // Añadir un nuevo vehículo con pasajeros y foto obligatoria
    public Vehicle addVehicleWithPassengers(String name, String driver, String contactNumber, 
        String coDriver, String passenger1, String passenger2, MultipartFile photoFile) throws IOException {
        if (photoFile == null || photoFile.isEmpty()) {
            throw new IllegalArgumentException("La foto del vehículo es obligatoria");
        }

        Vehicle vehicle = new Vehicle(name, driver, contactNumber);
        vehicle.setCoDriver(coDriver);
        vehicle.setPassenger1(passenger1);
        vehicle.setPassenger2(passenger2);
        vehicle.setPhotoFromFile(photoFile);
        return vehicleRepository.save(vehicle);
    }

    // Eliminar un vehículo por ID
    public boolean deleteVehicleById(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtener vehículo por ID
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    // Verificar si existe un vehículo
    public boolean vehicleExists(Long id) {
        return vehicleRepository.existsById(id);
    }

    // Método para añadir pasajero a un vehículo existente
    public boolean addPassenger(Long vehicleId, String passengerName) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            boolean added = vehicle.addPassenger(passengerName);
            if (added) {
                vehicleRepository.save(vehicle);
            }
            return added;
        }
        return false;
    }

    // Método para limpiar pasajeros de un vehículo
    public void clearPassengers(Long vehicleId) {
        vehicleRepository.findById(vehicleId).ifPresent(vehicle -> {
            vehicle.clearPassengers();
            vehicleRepository.save(vehicle);
        });
    }

}