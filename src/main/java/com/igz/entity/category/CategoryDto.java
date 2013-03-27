package com.igz.entity.category;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name="cat")
public class CategoryDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id private Long id;
	@Index private String name;
	private String picture;

	private Date creationDate;

	public CategoryDto() {}
	
	/**
	 * Constructor for use with jersey-jackson deserialization
	 * @param id
	 */
	public CategoryDto(String idString) {
		id = Long.valueOf(idString);
	}
	
	public CategoryDto(Long idString) {
		id = idString;
	}
	
	public Key<CategoryDto> getKey() {
		return Key.create(CategoryDto.class, id);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
