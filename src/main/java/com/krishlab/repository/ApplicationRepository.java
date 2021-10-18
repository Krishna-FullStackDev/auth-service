package com.krishlab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishlab.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	Optional<Application> findById(Long id);
	
	Optional<Application> findByAppName(String appName);
}
