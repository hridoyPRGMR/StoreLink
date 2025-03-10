package com.storelink.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="role_seq")
    @SequenceGenerator(name="role_seq",sequenceName="role_sequence",allocationSize=1)
    private long id;
    
    @Column(nullable=false,unique=true)
    @NotNull(message = "Role name is required.")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
