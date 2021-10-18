package com.krishlab.repository;



import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.krishlab.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>  {

	Optional<UserEntity> findById(Long Id);
	
	Optional<UserEntity> findByUsername(String username); 
	
	UserEntity findByUsernameOrEmail(String username , String email); 
	
	UserEntity findByUsernameOrEmailOrEmpId(String username , String email,String empId); 
	
	
}
