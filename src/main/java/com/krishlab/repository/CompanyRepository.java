package com.krishlab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishlab.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findById(Long id);
	
	Optional<List<Company>> findByMasId(Long masId);
	
	Optional<Company> findByCompName(String compName);
	
}
