package com.krishlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishlab.entity.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

}
