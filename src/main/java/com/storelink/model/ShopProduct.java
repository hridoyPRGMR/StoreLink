package com.storelink.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="shop_products")
public class ShopProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="shop_product_seq")
	@SequenceGenerator(name="shop_product_seq",sequenceName = "shop_product_seq",allocationSize=1)
	private long id;
	
	@ManyToOne(fetch  = FetchType.LAZY)
	@JoinColumn(name="shop_id",nullable=false)
	private Shop shop;
	
	
	@ManyToOne(fetch  = FetchType.LAZY)
	@JoinColumn(name="product_id",nullable=false)
	private Product product;
	
	@OneToOne
	@JoinColumn(name="attribute_id",referencedColumnName = "id")
	private Attribute attribute;
	
	@Column(nullable=false)
	private int stock;
	
	private LocalDate createdAt;
	private LocalDate updatedAt;
	
	public ShopProduct(Shop shop, Product product, Attribute attribute,int stock) {
		this.shop=shop;
		this.product=product;
		this.attribute = attribute;
		this.stock = stock;
	}
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDate.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDate.now();
	}


}
