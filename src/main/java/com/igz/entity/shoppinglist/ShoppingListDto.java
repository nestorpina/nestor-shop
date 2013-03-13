package com.igz.entity.shoppinglist;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.igz.entity.user.UserDto;

@Entity(name="SL")
public class ShoppingListDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id private Long id;
	@Index private String name;
	@Parent private Key<UserDto> owner;
	private Date creationDate;
	private Boolean open;
	public Long getId() {
		return id;
	}
	
	public Key<ShoppingListDto> getKey() {
		return Key.create(ShoppingListDto.class, id);
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
	public Key<UserDto> getOwner() {
		return owner;
	}
	public void setOwner(Key<UserDto> owner) {
		this.owner = owner;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	} 

	
}
