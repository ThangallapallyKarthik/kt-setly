package com.kt.setly.group.dto;

import com.kt.setly.group.entity.GroupType;

public record GroupResponse(
        Long id,
        String name,
        String description,
        GroupType groupType,
        String baseCurrency,
        Long createdByUserId
) {
}
