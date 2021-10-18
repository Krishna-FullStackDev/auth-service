package com.krishlab.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements Serializable {

	private static final long serialVersionUID = -1128990634276428830L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String menuname;
	private Long masId;
	private Long appId;
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
	
	@ManyToMany(mappedBy="menus")
	private List<Role> roles;
}
