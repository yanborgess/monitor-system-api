package com.yan.security.monitorSystem.controllers.dtos;

import com.yan.security.monitorSystem.models.user.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
