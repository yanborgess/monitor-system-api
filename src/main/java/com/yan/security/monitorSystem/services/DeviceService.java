package com.yan.security.monitorSystem.services;


import com.yan.security.monitorSystem.controllers.dtos.DeviceRequestDTO;
import com.yan.security.monitorSystem.controllers.dtos.DeviceResponseDTO;
import com.yan.security.monitorSystem.controllers.dtos.DeviceStatusRequestDTO;
import com.yan.security.monitorSystem.models.Device;
import com.yan.security.monitorSystem.models.DeviceLog;
import com.yan.security.monitorSystem.repositories.DeviceLogRepository;
import com.yan.security.monitorSystem.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository repository;

    public DeviceResponseDTO saveDevice(DeviceRequestDTO dto) {
        Device device = new Device();
        device.setName(dto.name());
        device.setType(dto.type());
        device.setLocation(dto.location());
        device.setActive(true); // Regra de negócio: todo novo dispositivo nasce ativo

        Device savedDevice = repository.save(device);
        return new DeviceResponseDTO(savedDevice);
    }

    public List<DeviceResponseDTO> listAll() {
        return repository.findAll()
                .stream()
                .map(DeviceResponseDTO::new) // Converte cada Device da lista em DeviceResponseDTO
                .toList();
    }

    public DeviceResponseDTO findById(Long id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado com o ID: " + id));

        return new DeviceResponseDTO(device);
    }

    public void delete(Long id) {
        // É boa prática verificar se o ID existe antes de tentar deletar
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            // Se não existir, você pode lançar uma exceção ou apenas logar
            throw new RuntimeException("Dispositivo não encontrado com o ID: " + id);
        }
    }

    public DeviceResponseDTO update(Long id, DeviceRequestDTO dto) {
        // 1. Busca o dispositivo existente
        Device device = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não foi possível atualizar: ID não encontrado."));

        // 2. Atualiza os campos com os dados que vieram do DTO
        device.setName(dto.name());
        device.setType(dto.type());
        device.setLocation(dto.location());

        // 3. Salva a versão atualizada
        Device updatedDevice = repository.save(device);

        // 4. Retorna o DTO de resposta
        return new DeviceResponseDTO(updatedDevice);
    }



    @Autowired
    private DeviceLogRepository logRepository; // Injetamos o novo repositório

    public DeviceResponseDTO updateStatus(Long id, DeviceStatusRequestDTO dto) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado."));

        // Aplicando a regra de negócio que já criamos
        if (!dto.active() && device.getLocation().equalsIgnoreCase("Portaria")) {
            throw new RuntimeException("Segurança violada: Câmeras da Portaria não podem ser desativadas!");
        }

        // 1. Atualiza o status do dispositivo
        device.setActive(dto.active());
        repository.save(device);

        // 2. CRIA O LOG DE AUDITOR
        DeviceLog log = new DeviceLog();
        log.setDevice(device);
        log.setAction("STATUS_CHANGE");
        log.setDetails("Status alterado para: " + (dto.active() ? "ATIVO" : "INATIVO"));
        log.setTimestamp(LocalDateTime.now()); // Pega a hora exata do servidor

        logRepository.save(log); // Salva o histórico

        return new DeviceResponseDTO(device);
    }

    public  List<DeviceResponseDTO> listOfflineDevices() {
        return repository.findByActiveFalse()
                .stream()
                .map(DeviceResponseDTO::new)
                .toList();
    }

}