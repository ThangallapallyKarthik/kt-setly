package com.kt.setly.group.controller;

import com.kt.setly.common.response.ApiResponse;
import com.kt.setly.group.dto.AddGroupMemberRequest;
import com.kt.setly.group.dto.CreateGroupRequest;
import com.kt.setly.group.dto.GroupMemberResponse;
import com.kt.setly.group.dto.GroupResponse;
import com.kt.setly.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Groups", description = "Group management APIs")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "Create group")
    public ApiResponse<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        return ApiResponse.<GroupResponse>builder()
                .success(true)
                .message("Group created successfully")
                .data(groupService.createGroup(request))
                .build();
    }

    @PostMapping("/{groupId}/members")
    @Operation(summary = "Add member to group")
    public ApiResponse<Void> addMember(@PathVariable Long groupId,
                                       @Valid @RequestBody AddGroupMemberRequest request) {
        groupService.addMember(groupId, request);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Group member added successfully")
                .build();
    }

    @GetMapping("/{groupId}/members")
    @Operation(summary = "Get all members of a group")
    public ApiResponse<List<GroupMemberResponse>> getGroupMembers(@PathVariable Long groupId) {
        return ApiResponse.<List<GroupMemberResponse>>builder()
                .success(true)
                .message("Group members fetched successfully")
                .data(groupService.getGroupMembers(groupId))
                .build();
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    @Operation(summary = "Remove member from group")
    public ApiResponse<Void> removeMember(@PathVariable Long groupId,
                                          @PathVariable Long userId) {
        groupService.removeMember(groupId, userId);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Group member removed successfully")
                .build();
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "Get group by id")
    public ApiResponse<GroupResponse> getGroup(@PathVariable Long groupId) {
        return ApiResponse.<GroupResponse>builder()
                .success(true)
                .message("Group fetched successfully")
                .data(groupService.getGroup(groupId))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all groups")
    public ApiResponse<List<GroupResponse>> getAllGroups() {
        return ApiResponse.<List<GroupResponse>>builder()
                .success(true)
                .message("Groups fetched successfully")
                .data(groupService.getAllGroups())
                .build();
    }
}
