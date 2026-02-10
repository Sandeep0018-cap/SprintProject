package com.example.sprintdb.dto;

public class CategoryDto {
    private Long categoryId;
    private String name;

    public CategoryDto() {}
    public CategoryDto(Long id, String name) { this.categoryId = id; this.name = name; }

    public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
