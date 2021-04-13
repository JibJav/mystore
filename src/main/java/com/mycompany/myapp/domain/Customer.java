package com.mycompany.myapp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "customer")
    private Set<Ordar> ordars = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "customer_product",
               joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Set<Product> products = new HashSet<>();

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

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Customer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Customer timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public Customer user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ordar> getOrdars() {
        return ordars;
    }

    public Customer ordars(Set<Ordar> ordars) {
        this.ordars = ordars;
        return this;
    }

    public Customer addOrdar(Ordar ordar) {
        this.ordars.add(ordar);
        ordar.setCustomer(this);
        return this;
    }

    public Customer removeOrdar(Ordar ordar) {
        this.ordars.remove(ordar);
        ordar.setCustomer(null);
        return this;
    }

    public void setOrdars(Set<Ordar> ordars) {
        this.ordars = ordars;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Customer products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Customer addProduct(Product product) {
        this.products.add(product);
        product.getCustomers().add(this);
        return this;
    }

    public Customer removeProduct(Product product) {
        this.products.remove(product);
        product.getCustomers().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
