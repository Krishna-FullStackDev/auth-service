package com.krishlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishlab.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{

}
