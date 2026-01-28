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
public class PhoneNumber implements Serializable {

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private boolean verified;

    public PhoneNumber(String value) {
        String cleaned = clean(value);
        if (!validate(cleaned)) {
            throw new IllegalArgumentException("Número de telefone inválido: " + value);
        }
        this.value = clean(value);
        this.verified = false;
    }

    private PhoneNumber(String value, boolean verified) {
        this.value = value;
        this.verified = verified;
    }

    public PhoneNumber verify() {
        return new PhoneNumber(this.value, true);
    }

    private String clean(String number) {
        if (number == null) return "";
        return number.replaceAll("\\D", "");
    }

    private boolean validate(String number) {
        return number.length() >= 10 && number.length() <= 11;
    }


    public String getFormatted() {
        if (value.length() == 11) {
            return value.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        if (value.length() == 10) {
            return value.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return verified == that.verified && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, verified);
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}