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
	private Integer itemsTotal;
	private Integer itemsBought;
	private Integer itemsDistinct;

	
	public ShoppingListDto() {
		super();
		creationDate = new Date();
		open = Boolean.TRUE;
		itemsTotal = 0;
		itemsBought = 0;
		itemsDistinct = 0;
		
	}

	public Long getId() {
		return id;
	}
	
	public Key<ShoppingListDto> getKey() {
		return Key.create(owner, ShoppingListDto.class, id);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingListDto other = (ShoppingListDto) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	public Integer getItemsTotal() {
		return itemsTotal;
	}

	public void setItemsTotal(Integer itemsTotal) {
		this.itemsTotal = itemsTotal;
	}

	public Integer getItemsDistinct() {
		return itemsDistinct;
	}

	public void setItemsDistinct(Integer itemsDistinct) {
		this.itemsDistinct = itemsDistinct;
	}

	public Integer getItemsBought() {
		return itemsBought;
	}

	public void setItemsBought(Integer itemsBought) {
		this.itemsBought = itemsBought;
	} 
	
}
