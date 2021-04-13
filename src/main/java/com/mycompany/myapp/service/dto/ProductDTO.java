package com.mycompany.myapp.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import com.mycompany.myapp.domain.enumeration.AvailableStatus;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Product} entity.
 */
public class ProductDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String brand;

    private AvailableStatus status;

    private Instant ordarDate;

    private Double price;

    private Integer quantity;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public AvailableStatus getStatus() {
        return status;
    }

    public void setStatus(AvailableStatus status) {
        this.status = status;
    }

    public Instant getOrdarDate() {
        return ordarDate;
    }

    public void setOrdarDate(Instant ordarDate) {
        this.ordarDate = ordarDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        return id != null && id.equals(((ProductDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
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
