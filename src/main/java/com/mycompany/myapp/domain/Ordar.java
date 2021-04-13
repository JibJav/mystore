package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.OrdarStatus;

/**
 * A Ordar.
 */
@Entity
@Table(name = "ordar")
public class Ordar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ordarwithname", nullable = false)
    private String ordarwithname;

    @NotNull
    @Column(name = "ordar_id", nullable = false)
    private Long ordarId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrdarStatus status;

    @Column(name = "ordar_date")
    private Instant ordarDate;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "total_items")
    private Integer totalItems;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "ordars", allowSetters = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "ordar_product",
               joinColumns = @JoinColumn(name = "ordar_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "ordars", allowSetters = true)
    private Store store;

    @ManyToOne
    @JsonIgnoreProperties(value = "ordars", allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdarwithname() {
        return ordarwithname;
    }

    public Ordar ordarwithname(String ordarwithname) {
        this.ordarwithname = ordarwithname;
        return this;
    }

    public void setOrdarwithname(String ordarwithname) {
        this.ordarwithname = ordarwithname;
    }

    public Long getOrdarId() {
        return ordarId;
    }

    public Ordar ordarId(Long ordarId) {
        this.ordarId = ordarId;
        return this;
    }

    public void setOrdarId(Long ordarId) {
        this.ordarId = ordarId;
    }

    public OrdarStatus getStatus() {
        return status;
    }

    public Ordar status(OrdarStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrdarStatus status) {
        this.status = status;
    }

    public Instant getOrdarDate() {
        return ordarDate;
    }

    public Ordar ordarDate(Instant ordarDate) {
        this.ordarDate = ordarDate;
        return this;
    }

    public void setOrdarDate(Instant ordarDate) {
        this.ordarDate = ordarDate;
    }

    public Long getAmount() {
        return amount;
    }

    public Ordar amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public Ordar totalItems(Integer totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public User getUser() {
        return user;
    }

    public Ordar user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Ordar products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Ordar addProduct(Product product) {
        this.products.add(product);
        product.getOrdars().add(this);
        return this;
    }

    public Ordar removeProduct(Product product) {
        this.products.remove(product);
        product.getOrdars().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Store getStore() {
        return store;
    }

    public Ordar store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Ordar customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ordar)) {
            return false;
        }
        return id != null && id.equals(((Ordar) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ordar{" +
            "id=" + getId() +
            ", ordarwithname='" + getOrdarwithname() + "'" +
            ", ordarId=" + getOrdarId() +
            ", status='" + getStatus() + "'" +
            ", ordarDate='" + getOrdarDate() + "'" +
            ", amount=" + getAmount() +
            ", totalItems=" + getTotalItems() +
            "}";
    }
}
