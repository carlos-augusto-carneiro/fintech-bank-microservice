package com.fintech.jakarta.domain.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address implements Serializable {

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;

    @Column(nullable = false)
    private String zip; 

    @Column(nullable = false)
    private String country;


    public Address(String street, String neighborhood, String city, String state, String zip, String country) {
        validate(street, "Rua");
        validate(neighborhood, "Bairro");
        validate(city, "Cidade");
        validate(state, "Estado");
        validate(zip, "CEP");
        validate(country, "País");

        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    private void validate(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo '" + fieldName + "' do endereço não pode ser vazio.");
        }
    }

    public String getFullAddress() {
        return String.format("%s, %s - %s/%s, %s (CEP: %s)",
                street, neighborhood, city, state, country, zip);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(city, address.city) &&
                Objects.equals(state, address.state) &&
                Objects.equals(zip, address.zip) &&
                Objects.equals(country, address.country) &&
                Objects.equals(neighborhood, address.neighborhood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zip, country, neighborhood);
    }

    @Override
    public String toString() {
        return getFullAddress();
    }
}