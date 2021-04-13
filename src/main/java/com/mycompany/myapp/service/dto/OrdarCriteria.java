package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.OrdarStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Ordar} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.OrdarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ordars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrdarCriteria implements Serializable, Criteria {
    /**
     * Class for filtering OrdarStatus
     */
    public static class OrdarStatusFilter extends Filter<OrdarStatus> {

        public OrdarStatusFilter() {
        }

        public OrdarStatusFilter(OrdarStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrdarStatusFilter copy() {
            return new OrdarStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ordarwithname;

    private LongFilter ordarId;

    private OrdarStatusFilter status;

    private InstantFilter ordarDate;

    private LongFilter amount;

    private IntegerFilter totalItems;

    private LongFilter userId;

    private LongFilter productId;

    private LongFilter storeId;

    private LongFilter customerId;

    public OrdarCriteria() {
    }

    public OrdarCriteria(OrdarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ordarwithname = other.ordarwithname == null ? null : other.ordarwithname.copy();
        this.ordarId = other.ordarId == null ? null : other.ordarId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.ordarDate = other.ordarDate == null ? null : other.ordarDate.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.totalItems = other.totalItems == null ? null : other.totalItems.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public OrdarCriteria copy() {
        return new OrdarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOrdarwithname() {
        return ordarwithname;
    }

    public void setOrdarwithname(StringFilter ordarwithname) {
        this.ordarwithname = ordarwithname;
    }

    public LongFilter getOrdarId() {
        return ordarId;
    }

    public void setOrdarId(LongFilter ordarId) {
        this.ordarId = ordarId;
    }

    public OrdarStatusFilter getStatus() {
        return status;
    }

    public void setStatus(OrdarStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getOrdarDate() {
        return ordarDate;
    }

    public void setOrdarDate(InstantFilter ordarDate) {
        this.ordarDate = ordarDate;
    }

    public LongFilter getAmount() {
        return amount;
    }

    public void setAmount(LongFilter amount) {
        this.amount = amount;
    }

    public IntegerFilter getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(IntegerFilter totalItems) {
        this.totalItems = totalItems;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getStoreId() {
        return storeId;
    }

    public void setStoreId(LongFilter storeId) {
        this.storeId = storeId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrdarCriteria that = (OrdarCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ordarwithname, that.ordarwithname) &&
            Objects.equals(ordarId, that.ordarId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(ordarDate, that.ordarDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(totalItems, that.totalItems) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(storeId, that.storeId) &&
            Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ordarwithname,
        ordarId,
        status,
        ordarDate,
        amount,
        totalItems,
        userId,
        productId,
        storeId,
        customerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdarCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ordarwithname != null ? "ordarwithname=" + ordarwithname + ", " : "") +
                (ordarId != null ? "ordarId=" + ordarId + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (ordarDate != null ? "ordarDate=" + ordarDate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (totalItems != null ? "totalItems=" + totalItems + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
                (storeId != null ? "storeId=" + storeId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}
