package com.krishlab.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
	
	private static final long serialVersionUID = -189861859955111591L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true)
	private String username;
	private String password;
	private String name;
	private String email;
	private String phone;
	private String empId;
	
	@Column(name = "profile_pic")
	private String profilePic;
	
	private boolean enabled = false;
	
	@Column(name = "accountNonLocked")
	private boolean accountNonLocked = true;

    @Column(name = "credentialsNonExpired")
    private boolean credentialsNonExpired = true;
    
    @Column(name = "pwd_reset_otp")
    private String otp = "";
  
    @JsonIgnore
	@Column(name = "created_at")
	@CreationTimestamp
	private Instant createdAt;
	
	@JsonIgnore
	@Column(name = "updated_at")
	@UpdateTimestamp
	private Instant updatedAt;
	
	@JsonIgnore
	@Column(name = "updated_by")
	private Long updatedBy = 1L; 
	
	@ManyToMany(fetch = FetchType.EAGER )
	@JoinTable(name="user_roles", joinColumns = {@JoinColumn(name="user_id",referencedColumnName = "id")},
	inverseJoinColumns = {@JoinColumn(name="role_id",referencedColumnName = "id")})
	private Set<Role> roles;
	
	@ManyToMany(fetch = FetchType.LAZY )
	@JoinTable(name="user_companies", joinColumns = {@JoinColumn(name="user_id",referencedColumnName = "id")},
	inverseJoinColumns = {@JoinColumn(name="comp_id",referencedColumnName = "id")})
	private Set<Company> companies;

}
