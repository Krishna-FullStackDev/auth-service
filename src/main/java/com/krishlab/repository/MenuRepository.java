package com.krishlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishlab.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>{

}
