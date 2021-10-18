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
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Role implements Serializable {

	private static final long serialVersionUID = 5185406578804767568L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true,name = "role_name")
	private String roleName;
	
	@Column(name = "role_desc")
	private String roleDesc;
	
	private boolean enabled = true;
	
	@JsonIgnore
	@Column(name = "created_at")
	@CreationTimestamp
	private Instant createdAt;
	
	@JsonIgnore
	@Column(name = "updated_at")
	@UpdateTimestamp
	private Instant updatedAt;
	
	@JsonIgnore
	private Long updatedBy=1L; 
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="role_app_access",joinColumns = {@JoinColumn(name="role_id",referencedColumnName ="id" )}
	,inverseJoinColumns = {@JoinColumn(name="app_id",referencedColumnName = "id")})
	private Set<Application> apps;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="role_menu_access",joinColumns = {@JoinColumn(name="role_id",referencedColumnName ="id" )}
	,inverseJoinColumns = {@JoinColumn(name="menu_id",referencedColumnName = "id")})
	private Set<Menu> menus;
}
