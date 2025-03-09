package com.storelink.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "variations")
public class Variation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="variation_seq")
    @SequenceGenerator(name="variation_seq",sequenceName="variation_sequence",allocationSize=1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String attributeType;

    @Column(nullable = false, length = 50)
    private String unit;
    
    @JsonIgnore
    @JsonManagedReference
    @OneToOne(mappedBy = "variation",cascade = CascadeType.ALL)
    private Product product;

    @JsonManagedReference
    @OneToMany(mappedBy = "variation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes = new ArrayList<>();
}
