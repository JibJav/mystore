package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Store} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StoreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private InstantFilter timestamp;

    private LongFilter ordarId;

    public StoreCriteria() {
    }

    public StoreCriteria(StoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.ordarId = other.ordarId == null ? null : other.ordarId.copy();
    }

    @Override
    public StoreCriteria copy() {
        return new StoreCriteria(this);
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

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
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
        final StoreCriteria that = (StoreCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(ordarId, that.ordarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        address,
        timestamp,
        ordarId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (ordarId != null ? "ordarId=" + ordarId + ", " : "") +
            "}";
    }

}
