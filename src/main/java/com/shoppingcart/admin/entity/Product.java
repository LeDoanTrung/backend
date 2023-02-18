package com.shoppingcart.admin.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "products")
public class Product extends IdBaseEntity{
	@Column(length = 255, nullable = false, unique = true)
	private String name;
	
	@Column(length = 255, nullable = false, unique = true)
	private String alias;
	
	@Column(length = 512, nullable = false)
	private String shortDescription;
	
	@Column(length = 4096, nullable =false)
	private String fullDescription;
	
	@Column(nullable = false)
	private String mainImage;
	
	@Column(nullable = false)
	private Date createTime;
	
	private Date updateTime;
	
	private boolean enabled;
	
	private boolean inStock;
	
	private float cost;
	
	private float price;
	
	private float discountPercent;
	
	private float length;
	
	private float width;
	
	private float height;
	
	private float weight;
	
	@OneToMany(mappedBy = "product")
	private Set<Brand> brands;
	
	@OneToOne(mappedBy = "product")
	private Category categories;

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

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Set<Brand> getBrands() {
		return brands;
	}

	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}

	public Category getCategories() {
		return categories;
	}

	public void setCategories(Category categories) {
		this.categories = categories;
	}

	public Product(String name, String alias, String shortDescription, String fullDescription, String mainImage,
			Date createTime, Date updateTime, boolean enabled, boolean inStock, float cost, float price,
			float discountPercent, float length, float width, float height, float weight, Set<Brand> brands,
			Category categories) {
		super();
		this.name = name;
		this.alias = alias;
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
		this.mainImage = mainImage;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.enabled = enabled;
		this.inStock = inStock;
		this.cost = cost;
		this.price = price;
		this.discountPercent = discountPercent;
		this.length = length;
		this.width = width;
		this.height = height;
		this.weight = weight;
		this.brands = brands;
		this.categories = categories;
	}
	
	
	public Product() {
		super();
	}
	
	@Transient
	public String getPhotosImagePath() {
		if (id == null || mainImage == null ) return "/images/image-thumbnail.png"; //nếu ko có image thì hiển thị
		
		return "/product-mainImages/" + this.id + "/"+ this.mainImage;
	}
	
	
}
