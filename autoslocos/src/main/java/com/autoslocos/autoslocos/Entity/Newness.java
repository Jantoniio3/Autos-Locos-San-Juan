package com.autoslocos.autoslocos.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Entity
public class Newness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    // Empty constructor
    public Newness() {
    }

    // Constructor with parameters (sin archivo)
    public Newness(LocalDate date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    // Método conveniente para manejar archivos
    public void setFileInfo(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            this.fileName = file.getOriginalFilename();
            this.fileType = file.getContentType();
            this.fileSize = file.getSize();
            this.fileData = file.getBytes();
        }
    }
}