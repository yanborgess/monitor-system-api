package com.yan.security.monitorSystem.controllers;

import com.yan.security.monitorSystem.controllers.dtos.DeviceRequestDTO;
import com.yan.security.monitorSystem.controllers.dtos.DeviceResponseDTO;
import com.yan.security.monitorSystem.controllers.dtos.DeviceStatusRequestDTO;
import com.yan.security.monitorSystem.services.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
})

@RestController
@RequestMapping("/api/devices")
@Tag(name = "Dispositivos", description = "Gerenciamento de sensores e câmeras de segurança")
public class DeviceController {

    @Autowired
    private DeviceService service;

    @Operation(summary = "Cadastra um novo dispositivo")
    @PostMapping
    public DeviceResponseDTO create(@RequestBody @Valid DeviceRequestDTO dto) {
        return service.saveDevice(dto);
    }

    @Operation(summary = "Lista todos os dispositivos")
    @GetMapping
    public List<DeviceResponseDTO> getAll() {
        return service.listAll();
    }

    @Operation(summary = "Busca dispositivo por id")
    @GetMapping("/{id}")
    public DeviceResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Atualiza dispositivo por id")
    @PutMapping("/{id}")
    public DeviceResponseDTO update(@PathVariable Long id, @RequestBody @Valid DeviceRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Atualiza status por id ")
    @PatchMapping("/{id}/status")
    public DeviceResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestBody DeviceStatusRequestDTO dto) {

        return service.updateStatus(id, dto);
    }

    @Operation(summary = "Filtra apenas dispositivos offline ")
    @GetMapping("/offline")
    public List<DeviceResponseDTO> getOffline() {
        return service.listOfflineDevices();
    }
}