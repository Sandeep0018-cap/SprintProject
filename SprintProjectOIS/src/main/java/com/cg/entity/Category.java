package com.cg.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity // Marks this class as a persistent database entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name")) // Configures table-level constraints
public class Category {

    @Id // Marks the field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures auto-increment behavior for the ID
    @Column(name = "category_id") // Maps the field to the specific database column name
    private Long categoryId;

    @NotBlank // Validation: Ensures the name is not null or whitespace
    @Size(max = 120) // Validation: Limits character count for database compatibility
    @Column(nullable = false, length = 120) // Enforces non-nullability and length at the schema level
    private String name;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
