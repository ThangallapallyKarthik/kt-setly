package com.kt.setly.group.service;

import com.kt.setly.common.exception.BadRequestException;
import com.kt.setly.common.exception.ResourceNotFoundException;
import com.kt.setly.group.dto.AddGroupMemberRequest;
import com.kt.setly.group.dto.CreateGroupRequest;
import com.kt.setly.group.dto.GroupMemberResponse;
import com.kt.setly.group.dto.GroupResponse;
import com.kt.setly.group.entity.*;
import com.kt.setly.group.repository.ExpenseGroupRepository;
import com.kt.setly.group.repository.GroupMemberRepository;
import com.kt.setly.user.entity.User;
import com.kt.setly.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final ExpenseGroupRepository expenseGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserService userService;

    public GroupResponse createGroup(CreateGroupRequest request) {
        User creator = userService.getUserEntity(request.createdByUserId());
        OffsetDateTime now = OffsetDateTime.now();

        ExpenseGroup group = ExpenseGroup.builder()
                .name(request.name().trim())
                .description(request.description())
                .groupType(request.groupType())
                .baseCurrency(request.baseCurrency().trim().toUpperCase())
                .createdBy(creator)
                .createdAt(now)
                .updatedAt(now)
                .build();

        ExpenseGroup savedGroup = expenseGroupRepository.save(group);

        GroupMember ownerMember = GroupMember.builder()
                .group(savedGroup)
                .user(creator)
                .role(GroupMemberRole.OWNER)
                .status(GroupMemberStatus.ACTIVE)
                .joinedAt(now)
                .build();
        groupMemberRepository.save(ownerMember);

        return map(savedGroup);
    }

    public GroupResponse getGroup(Long groupId) {
        return map(getGroupEntity(groupId));
    }

    public List<GroupResponse> getAllGroups() {
        return expenseGroupRepository.findAll().stream().map(this::map).toList();
    }

    public void addMember(Long groupId, AddGroupMemberRequest request) {
        ExpenseGroup group = getGroupEntity(groupId);
        User user = userService.getUserEntity(request.userId());

        if (groupMemberRepository.existsByGroup_IdAndUser_Id(groupId, request.userId())) {
            throw new BadRequestException("User is already a member of this group");
        }

        groupMemberRepository.save(GroupMember.builder()
                .group(group)
                .user(user)
                .role(request.role())
                .status(GroupMemberStatus.ACTIVE)
                .joinedAt(OffsetDateTime.now())
                .build());
    }

    public List<GroupMemberResponse> getGroupMembers(Long groupId) {
        getGroupEntity(groupId); // Validate group exists
        return groupMemberRepository.findByGroup_Id(groupId).stream()
                .map(this::mapMember)
                .toList();
    }

    public void removeMember(Long groupId, Long userId) {
        getGroupEntity(groupId); // Validate group exists
        
        GroupMember member = groupMemberRepository.findByGroup_IdAndUser_Id(groupId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in group"));

        if (member.getRole() == GroupMemberRole.OWNER) {
            throw new BadRequestException("Cannot remove group owner");
        }

        groupMemberRepository.delete(member);
    }

    public ExpenseGroup getGroupEntity(Long groupId) {
        return expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found for id: " + groupId));
    }

    public void validateMember(Long groupId, Long userId) {
        if (!groupMemberRepository.existsByGroup_IdAndUser_Id(groupId, userId)) {
            throw new BadRequestException("User " + userId + " is not part of group " + groupId);
        }
    }

    private GroupResponse map(ExpenseGroup group) {
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getGroupType(),
                group.getBaseCurrency(),
                group.getCreatedBy().getId()
        );
    }

    private GroupMemberResponse mapMember(GroupMember member) {
        return new GroupMemberResponse(
                member.getId(),
                member.getGroup().getId(),
                member.getUser().getId(),
                member.getUser().getDisplayName(),
                member.getUser().getEmail(),
                member.getRole(),
                member.getStatus()
        );
    }
}
