package com.storelink.model;

import java.time.LocalDate;
import java.util.List;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shop")
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="shop_seq")
	@SequenceGenerator(name="shop_seq",sequenceName="shop_sequence",allocationSize=1)
	private long id;

	@Column(nullable = false, length = 250)
	private String name;

	@Column(length = 1000)
	private String introduction;

	private Double latitude;
	private Double longitude;

	@Column(columnDefinition = "geometry(Point,4326)", nullable = false)
	private Point location;

	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
	private User user;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	private List<ShopProduct> products;

	@Column(length = 500)
	private String profilePhotoUrl;

	@Column(length = 500)
	private String coverPhotoUrl;

	private LocalDate createdAt;
	private LocalDate updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDate.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDate.now();
	}

	@JsonGetter("location")
	public String getLocationAsWKT() {
		if (location != null) {
			return location.toText();
		}
		return null;
	}
}
