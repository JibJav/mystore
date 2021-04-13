package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.AvailableStatus;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AvailableStatus status;

    @Column(name = "ordar_date")
    private Instant ordarDate;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private Set<Ordar> ordars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public Product brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public AvailableStatus getStatus() {
        return status;
    }

    public Product status(AvailableStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AvailableStatus status) {
        this.status = status;
    }

    public Instant getOrdarDate() {
        return ordarDate;
    }

    public Product ordarDate(Instant ordarDate) {
        this.ordarDate = ordarDate;
        return this;
    }

    public void setOrdarDate(Instant ordarDate) {
        this.ordarDate = ordarDate;
    }

    public Double getPrice() {
        return price;
    }

    public Product price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Product customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Product addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.getProducts().add(this);
        return this;
    }

    public Product removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.getProducts().remove(this);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Ordar> getOrdars() {
        return ordars;
    }

    public Product ordars(Set<Ordar> ordars) {
        this.ordars = ordars;
        return this;
    }

    public Product addOrdar(Ordar ordar) {
        this.ordars.add(ordar);
        ordar.getProducts().add(this);
        return this;
    }

    public Product removeOrdar(Ordar ordar) {
        this.ordars.remove(ordar);
        ordar.getProducts().remove(this);
        return this;
    }

    public void setOrdars(Set<Ordar> ordars) {
        this.ordars = ordars;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", brand='" + getBrand() + "'" +
            ", status='" + getStatus() + "'" +
            ", ordarDate='" + getOrdarDate() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
