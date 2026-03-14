package com.kt.setly.group.dto;

import com.kt.setly.group.entity.GroupType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateGroupRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 500) String description,
        @NotNull GroupType groupType,
        @NotBlank @Size(min = 3, max = 3) String baseCurrency,
        @NotNull Long createdByUserId
) {
}
