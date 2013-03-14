package com.igz.entity.shoppinglistitem;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
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
	
	@Id private Long id;
	@Load private Ref<ProductDto> product;
	@Parent private Key<ShoppingListDto> shoppingListKey;
	@Index private Date dateAdded;
	private Date dateModified;
	@Index private Date dateBought;
	private Integer quantity;
	
	public Key<ShoppingListItemDto> getKey() {
		return Key.create(ShoppingListItemDto.class, id);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProductDto getProduct() {
		return product.get();
	}
	public void setProduct(ProductDto product) {
		this.product = Ref.create(Key.create(ProductDto.class, product.getId()));
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

	
}
