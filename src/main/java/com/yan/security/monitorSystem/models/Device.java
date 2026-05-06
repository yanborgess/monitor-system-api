package com.yan.security.monitorSystem.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_devices")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String location;
    private  boolean active;
}
