package com.kt.setly.group.dto;

import com.kt.setly.group.entity.GroupMemberRole;
import jakarta.validation.constraints.NotNull;

public record AddGroupMemberRequest(
        @NotNull Long userId,
        @NotNull GroupMemberRole role
) {
}
