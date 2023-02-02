package com.shoppingcart.admin.entity;



import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="categories")
public class Category extends IdBaseEntity{
	@Column(length = 128, nullable =  false, unique = true)
	private String name;
	
	@Column(length = 64, nullable = false)
	private String alias;
	
	@Column(length = 128)
	private String image;
	
	private boolean enabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category(String name, String alias, String image, boolean enabled) {
		super();
		this.name = name;
		this.alias = alias;
		this.image = image;
		this.enabled = enabled;
	}

	public Category() {
		super();
	}
	
	@Override
	public String toString() {
		return "Category [id="+ id + ", name =" + name + ", alias =" + alias;
	}
	
	@Transient
	public String getPhotosImagePath() {
		if (id == null || image == null ) return "/images/image-thumbnail.png"; //nếu ko có image thì hiển thị
		
		return "/category-photos/" + this.id + "/"+ this.image;
	}
	
}
