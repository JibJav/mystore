package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.AvailableStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Product} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {
    /**
     * Class for filtering AvailableStatus
     */
    public static class AvailableStatusFilter extends Filter<AvailableStatus> {

        public AvailableStatusFilter() {
        }

        public AvailableStatusFilter(AvailableStatusFilter filter) {
            super(filter);
        }

        @Override
        public AvailableStatusFilter copy() {
            return new AvailableStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter brand;

    private AvailableStatusFilter status;

    private InstantFilter ordarDate;

    private DoubleFilter price;

    private IntegerFilter quantity;

    private LongFilter customerId;

    private LongFilter ordarId;

    public ProductCriteria() {
    }

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.brand = other.brand == null ? null : other.brand.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.ordarDate = other.ordarDate == null ? null : other.ordarDate.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.ordarId = other.ordarId == null ? null : other.ordarId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public AvailableStatusFilter getStatus() {
        return status;
    }

    public void setStatus(AvailableStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getOrdarDate() {
        return ordarDate;
    }

    public void setOrdarDate(InstantFilter ordarDate) {
        this.ordarDate = ordarDate;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getOrdarId() {
        return ordarId;
    }

    public void setOrdarId(LongFilter ordarId) {
        this.ordarId = ordarId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(status, that.status) &&
            Objects.equals(ordarDate, that.ordarDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(ordarId, that.ordarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        brand,
        status,
        ordarDate,
        price,
        quantity,
        customerId,
        ordarId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (brand != null ? "brand=" + brand + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (ordarDate != null ? "ordarDate=" + ordarDate + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (ordarId != null ? "ordarId=" + ordarId + ", " : "") +
            "}";
    }

}
