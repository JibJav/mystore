package com.mycompany.myapp.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.mycompany.myapp.domain.enumeration.OrdarStatus;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Ordar} entity.
 */
public class OrdarDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String ordarwithname;

    @NotNull
    private Long ordarId;

    private OrdarStatus status;

    private Instant ordarDate;

    private Long amount;

    private Integer totalItems;


    private Long userId;

    private String userLogin;
    private Set<ProductDTO> products = new HashSet<>();

    private Long storeId;

    private Long customerId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdarwithname() {
        return ordarwithname;
    }

    public void setOrdarwithname(String ordarwithname) {
        this.ordarwithname = ordarwithname;
    }

    public Long getOrdarId() {
        return ordarId;
    }

    public void setOrdarId(Long ordarId) {
        this.ordarId = ordarId;
    }

    public OrdarStatus getStatus() {
        return status;
    }

    public void setStatus(OrdarStatus status) {
        this.status = status;
    }

    public Instant getOrdarDate() {
        return ordarDate;
    }

    public void setOrdarDate(Instant ordarDate) {
        this.ordarDate = ordarDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdarDTO)) {
            return false;
        }

        return id != null && id.equals(((OrdarDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdarDTO{" +
            "id=" + getId() +
            ", ordarwithname='" + getOrdarwithname() + "'" +
            ", ordarId=" + getOrdarId() +
            ", status='" + getStatus() + "'" +
            ", ordarDate='" + getOrdarDate() + "'" +
            ", amount=" + getAmount() +
            ", totalItems=" + getTotalItems() +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", products='" + getProducts() + "'" +
            ", storeId=" + getStoreId() +
            ", customerId=" + getCustomerId() +
            "}";
    }
}
