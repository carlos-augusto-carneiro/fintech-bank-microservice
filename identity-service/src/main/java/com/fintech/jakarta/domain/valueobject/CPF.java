package com.fintech.jakarta.domain.valueobject;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class CPF implements Serializable {

    @Column(nullable = false, length = 14)
    private String value;

    public CPF(String value) {
        if (!validate(value)) {
            throw new IllegalArgumentException("CPF inválido: " + value);
        }
        this.value = clean(value);
    }

    public String getFormatted() {
        return value.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }


    private String clean(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private boolean validate(String cpf) {
        if (cpf == null) return false;

        String num = clean(cpf);

        if (num.length() != 11) return false;

        if (num.matches("(\\d)\\1{10}")) return false;

        try {
            int sum = 0;
            int weight = 10;
            for (int i = 0; i < 9; i++) {
                sum += (num.charAt(i) - '0') * weight--;
            }

            int r = 11 - (sum % 11);
            char digit1 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            sum = 0;
            weight = 11;
            for (int i = 0; i < 10; i++) {
                sum += (num.charAt(i) - '0') * weight--;
            }

            r = 11 - (sum % 11);
            char digit2 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            return (digit1 == num.charAt(9)) && (digit2 == num.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return Objects.equals(value, cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
