package com.cg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class CategoryEntity {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank
	    @Size(max = 120)
	    @Column(nullable = false, length = 120)
	    private String name;

	    public Long getId() { 
	    	return id; 
	    	}
	    public String getName() { 
	    	return name; 
	    	}
	    public void setName(String name) {
	    	this.name = name;
	    	}
	}


