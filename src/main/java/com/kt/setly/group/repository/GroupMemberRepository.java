package com.kt.setly.group.repository;

import com.kt.setly.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroup_IdAndUser_Id(Long groupId, Long userId);
    List<GroupMember> findByGroup_Id(Long groupId);
    boolean existsByGroup_IdAndUser_Id(Long groupId, Long userId);
}
