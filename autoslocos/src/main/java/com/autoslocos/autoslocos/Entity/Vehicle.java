package com.autoslocos.autoslocos.Entity;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String driver; // Piloto principal

    @Column
    private String coDriver; // Copiloto (opcional)

    @Column
    private String passenger1; // Primer pasajero (opcional)

    @Column
    private String passenger2; // Segundo pasajero (opcional)

    @Column
    private String contactNumber; // Número de contacto (opcional)

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo; // Foto del vehículo (opcional)

    @Column
    private String photoType; // Tipo MIME de la foto (opcional)

    // Constructores
    public Vehicle() {
    }

    public Vehicle(String name, String driver) {
        this.name = name;
        this.driver = driver;
    }

    public Vehicle(String name, String driver, String contactNumber) {
        this.name = name;
        this.driver = driver;
        this.contactNumber = contactNumber;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCoDriver() {
        return coDriver;
    }

    public void setCoDriver(String coDriver) {
        this.coDriver = coDriver;
    }

    public String getPassenger1() {
        return passenger1;
    }

    public void setPassenger1(String passenger1) {
        this.passenger1 = passenger1;
    }

    public String getPassenger2() {
        return passenger2;
    }

    public void setPassenger2(String passenger2) {
        this.passenger2 = passenger2;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoType() {
        return photoType;
    }

    // Método para establecer la foto desde un MultipartFile
    public void setPhotoFromFile(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            this.photo = file.getBytes();
            this.photoType = file.getContentType();
        }
    }

    // Método para añadir pasajeros con validación
    public boolean addPassenger(String passengerName) {
        if (passenger1 == null) {
            passenger1 = passengerName;
            return true;
        } else if (passenger2 == null) {
            passenger2 = passengerName;
            return true;
        }
        return false; // No hay espacio para más pasajeros
    }

    // Método para limpiar todos los pasajeros
    public void clearPassengers() {
        passenger1 = null;
        passenger2 = null;
    }

    // Representación como String
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", driver='" + driver + '\'' +
                ", coDriver='" + coDriver + '\'' +
                ", passenger1='" + passenger1 + '\'' +
                ", passenger2='" + passenger2 + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}