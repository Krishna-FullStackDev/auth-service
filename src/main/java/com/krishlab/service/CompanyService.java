package com.krishlab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krishlab.entity.Company;
import com.krishlab.repository.CompanyRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository compRepo;
	
	public Company createNewRole(Company comp) {
		return compRepo.save(comp);
	}
}
