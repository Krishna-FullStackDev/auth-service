package com.krishlab.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Permission implements Serializable {

	private static final long serialVersionUID = -5252707072403933753L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private Long screenId;
	private Long roleId;
    private String permissionName;
    
 
}
