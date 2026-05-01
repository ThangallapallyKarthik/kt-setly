package com.kt.setly.group.dto;

import com.kt.setly.group.entity.GroupMemberRole;
import com.kt.setly.group.entity.GroupMemberStatus;

public record GroupMemberResponse(
        Long id,
        Long groupId,
        Long userId,
        String userDisplayName,
        String userEmail,
        GroupMemberRole role,
        GroupMemberStatus status
) {
}

