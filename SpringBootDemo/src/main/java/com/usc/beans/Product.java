package com.usc.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "usc_product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PRODUCT_SEQ")
	@SequenceGenerator(name = "PRODUCT_SEQ", sequenceName = "USC_PRODUCT_SEQ", allocationSize = 1)
	private int id;
	@Column(name = "productname", nullable = false)
	private String productname;
	@Column(name = "productbrand", nullable = false)
	private String productbrand;
	@Column(name = "price", nullable = false)
	@Positive
	private double price;
	@Column(name = "stock", nullable = false)
	@PositiveOrZero
	private int stock;
	@Column(name = "image", nullable = true)
	private String image;
	
	public Product() {
		super();
	}

	public Product(String name, String brand, @Positive double price, @PositiveOrZero int stock,
			String image) {
		super();
		this.productname = name;
		this.productbrand = brand;
		this.price = price;
		this.stock = stock;
		this.image = image;
	}
	
	public Product(String name) {
		super();
		System.out.println("Hello");
		this.productname = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getProductbrand() {
		return productbrand;
	}

	public void setProductbrand(String productbrand) {
		this.productbrand = productbrand;
	}

	public @Positive double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", productname=" + productname + ", productbrand=" + productbrand + ", price="
				+ price + ", stock=" + stock + ", image=" + image + "]";
	}	
}
