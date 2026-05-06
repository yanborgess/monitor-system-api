package com.yan.security.monitorSystem.controllers.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeviceRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String name,

        @NotBlank(message = "O tipo é obrigatório")
        String type,

        @NotBlank(message = "A localização é obrigatória")
        String location
) {}