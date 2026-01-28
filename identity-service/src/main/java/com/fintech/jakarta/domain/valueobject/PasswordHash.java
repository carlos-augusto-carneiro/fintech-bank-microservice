package com.fintech.jakarta.domain.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordHash implements Serializable {

    @Column(nullable = false)
    private String hash;

    private PasswordHash(String hash) {
        this.hash = hash;
    }

    public static PasswordHash createFromRaw(String rawPassword) {

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,64}$";

        if (rawPassword == null || !rawPassword.matches(regex)) {
            throw new IllegalArgumentException("A senha deve ter entre 8 e 16 caracteres, incluindo números, letras (maiúsculas/minúsculas) e símbolos.");
        }

        String generatedHash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
        return new PasswordHash(generatedHash);
    }

    public boolean verify(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.hash);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordHash that = (PasswordHash) o;
        return Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    @Override
    public String toString() {
        return "********";
    }
}