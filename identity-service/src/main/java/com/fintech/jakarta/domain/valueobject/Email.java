package com.fintech.jakarta.domain.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email implements Serializable {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private boolean verified;

    public Email(String value) {
        if (!validate(value)) {
            throw new IllegalArgumentException("Email inválido: " + value);
        }
        this.value = clean(value);
        this.verified = false;
    }

    private Email(String value, boolean verified) {
        this.value = value;
        this.verified = verified;
    }

    public Email verify() {
        return new Email(this.value, true);
    }

    public Email unverify() {
        return new Email(this.value, false);
    }

    private boolean validate(String email) {

        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    private String clean(String email) {

        return email.trim().toLowerCase();
    }

    public String getFormatted() {
        return this.value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return verified == email.verified && Objects.equals(value, email.value);
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