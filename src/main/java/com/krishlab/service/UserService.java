package com.krishlab.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.krishlab.entity.Company;
import com.krishlab.entity.Role;
import com.krishlab.entity.UserEntity;
import com.krishlab.repository.CompanyRepository;
import com.krishlab.repository.RoleRepository;
import com.krishlab.repository.UserRepository;

@Service
public class UserService  {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private CompanyRepository CompRepo;
	
	public UserEntity registerNewUser(UserEntity user) {
		
		Role role = roleRepo.findById(2L).get();
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		
		user.setRoles(roles);
		user.setEmail(user.getEmail().toLowerCase());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}
	
	public List<UserEntity> getUsers(){
		return userRepo.findAll();
	}
	
	public UserEntity findUserByNameOrEmail(String username,String emailId) {
		
		UserEntity usr=userRepo.findByUsernameOrEmail(username, emailId.toLowerCase());
        if(usr!=null && usr.isEnabled()) {
        	return usr;
        }
		return null;
    }
	
	public UserEntity findByUsernameOrEmailOrEmpId(String username,String emailId,String empId) {
		UserEntity usr=userRepo.findByUsernameOrEmailOrEmpId(username, emailId.toLowerCase(),empId);
        if(usr!=null && usr.isEnabled()) {
        	return usr;
        }
		return null;
    }
	
	
	public Optional <UserEntity> findUserByName(String username) {
        return userRepo.findByUsername(username);
    }
	
	public Optional <UserEntity> findById(Long userid) {
        return userRepo.findById(userid);
    }
	
	public void initRolesAndUser() {
		
		Optional<UserEntity> user=userRepo.findByUsername("SU");
		
		if(!user.isPresent()){
		//Creating Company
		Company comp=new Company();
		comp.setCompName("Framwork");
		comp.setCompDesc("Default Comapny");
		comp.setMasId(0L);
		CompRepo.save(comp);
		
		//Creating Role
		Role adminRole = new Role();
		adminRole.setRoleName("ROLE_SU");
		adminRole.setRoleDesc("Super User role");
		roleRepo.save(adminRole);
		
		Role userRole = new Role();
		userRole.setRoleName("ROLE_DEFAULT_USER");
		userRole.setRoleDesc("Default role for newly created record");
		roleRepo.save(userRole);
		
		//Creating User Detail
		UserEntity adminUser = new UserEntity();
		adminUser.setUsername("SU");
		adminUser.setName("Super User");
		adminUser.setPassword(passwordEncoder.encode("krish@911218"));
		adminUser.setEmail("admin@admin.com");
		adminUser.setPhone("9031661991");
		adminUser.setEnabled(true);
		adminUser.setProfilePic("admin.jpg");
		
		//adding Company
		Set<Company> userComp = new HashSet<>();
		userComp.add(comp);

		//adding Roles
		Set<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);
		adminRoles.add(userRole);
		
		//setting company and role to user
		adminUser.setCompanies(userComp);
		adminUser.setRoles(adminRoles);
		
		userRepo.save(adminUser);
		}
	}

	public Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        return authorities;
    }
	
	
}
