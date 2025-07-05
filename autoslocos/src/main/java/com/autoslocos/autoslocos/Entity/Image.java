package com.autoslocos.autoslocos.Entity;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] imageData;

    // Constructor vacío
    public Image() {
    }

    // Constructor con datos
    public Image(byte[] imageData) {
        this.imageData = imageData;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}