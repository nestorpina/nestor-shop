package com.igz.entity.shoppinglistitem;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import com.igz.entity.product.ProductDto;
import com.igz.entity.shoppinglist.ShoppingListDto;

/**
 * Shopping List Item
 * 
 * @author nestor.pina
 *
 */
@Entity(name="item")
public class ShoppingListItemDto implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String PRODUCT = "product";
	
	@Id private Long id;
	@Load @Index private ProductDto product;
	@Parent private Key<ShoppingListDto> shoppingListKey;
	@Index private Date dateAdded;
	private Date dateModified;
	@Index private Date dateBought;
	private Integer quantity;
	private Boolean bought;
	
	
	public ShoppingListItemDto() {
		super();
		quantity = 0;
		bought = Boolean.FALSE;
		dateAdded = new Date();
	}
	public Key<ShoppingListItemDto> getKey() {
		return Key.create(ShoppingListItemDto.class, id);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Key<ShoppingListDto> getShoppingListKey() {
		return shoppingListKey;
	}
	public void setShoppingListKey(Key<ShoppingListDto> shoppingListKey) {
		this.shoppingListKey = shoppingListKey;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	public Date getDateBought() {
		return dateBought;
	}
	public void setDateBought(Date dateBought) {
		this.dateBought = dateBought;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = prime * result + ((dateBought == null) ? 0 : dateBought.hashCode());
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((shoppingListKey == null) ? 0 : shoppingListKey.hashCode());
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
		ShoppingListItemDto other = (ShoppingListItemDto) obj;
		if (dateAdded == null) {
			if (other.dateAdded != null)
				return false;
		} else if (!dateAdded.equals(other.dateAdded))
			return false;
		if (dateBought == null) {
			if (other.dateBought != null)
				return false;
		} else if (!dateBought.equals(other.dateBought))
			return false;
		if (dateModified == null) {
			if (other.dateModified != null)
				return false;
		} else if (!dateModified.equals(other.dateModified))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (shoppingListKey == null) {
			if (other.shoppingListKey != null)
				return false;
		} else if (!shoppingListKey.equals(other.shoppingListKey))
			return false;
		return true;
	}
	public ProductDto getProduct() {
		return product;
	}
	public void setProduct(ProductDto product) {
		this.product = product;
	}
	public Boolean isBought() {
		return bought;
	}
	public void setBought(Boolean bought) {
		this.bought = bought;
	}

}
