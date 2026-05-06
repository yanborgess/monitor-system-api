package com.yan.security.monitorSystem.controllers.dtos;
import com.yan.security.monitorSystem.models.Device;

public record DeviceResponseDTO(
        Long id,
        String name,
        String type,
        String location,
        boolean active
) {

    public DeviceResponseDTO(Device device) {
        this(device.getId(), device.getName(), device.getType(), device.getLocation(), device.isActive());
    }
}