package com.krishlab.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screen implements Serializable {


	private static final long serialVersionUID = 5512505144070441292L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique=true, name="screen_name")
	private String screenName;
	
	@Column(name="screen_desc")
	private String screenDesc;
	
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
	
	
}
