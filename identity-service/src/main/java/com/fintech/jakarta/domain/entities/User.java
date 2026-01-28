package com.fintech.jakarta.domain.entities;


import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fintech.jakarta.domain.enuns.UserType;
import com.fintech.jakarta.domain.valueobject.Address;
import com.fintech.jakarta.domain.valueobject.CPF;
import com.fintech.jakarta.domain.valueobject.Email;
import com.fintech.jakarta.domain.valueobject.PasswordHash;
import com.fintech.jakarta.domain.valueobject.PhoneNumber;



@Entity
@Getter
@Table(name = "fintech_users")
@NoArgsConstructor
public class User {

    @Id
    @Column(unique = true, nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value",
                    column = @Column(name = "email", nullable = false, unique = true)),
            @AttributeOverride(name = "verified",
                    column = @Column(name = "email_verified", nullable = false))
    })
    private Email email;
    @Embedded
    @AttributeOverride(
            name = "hash",
            column = @Column(name = "password_hash", nullable = false)
    )
    private PasswordHash passwordHash;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",
                    column = @Column(name = "street", nullable = false)),
            @AttributeOverride(name = "neighborhood",
                    column = @Column(name = "neighborhood", nullable = false)),
            @AttributeOverride(name = "city",
                    column = @Column(name = "city", nullable = false)),
            @AttributeOverride(name = "state",
                    column = @Column(name = "state", nullable = false, length = 2)),
            @AttributeOverride(name = "zip",
                    column = @Column(name = "zip", nullable = false)),
            @AttributeOverride(name = "country",
                    column = @Column(name = "country", nullable = false))
    })
    private Address address;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value",
                    column = @Column(name = "phone_number")),
            @AttributeOverride(name = "verified",
                    column = @Column(name = "phone_verified"))
    })
    private PhoneNumber phoneNumber;
    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "cpf", nullable = false, unique = true)
    )
    private CPF cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_type")
    private UserType userType;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    public User(String firstName, String lastName, Email email, String rawPassword, CPF cpf, UserType userType, PhoneNumber phoneNumber, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = PasswordHash.createFromRaw(rawPassword);
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        this.userType = userType;
        this.isActive = true;
    }

    public void updateProfile(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) throw new IllegalArgumentException("Nome inválido");

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void updatePhoneNumber(String newNumber) {
        this.phoneNumber = new PhoneNumber(newNumber);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmail(String newEmailAddress) {
        if (this.email.getValue().equalsIgnoreCase(newEmailAddress)) {
            return;
        }
        this.email = new Email(newEmailAddress);
        this.updatedAt = LocalDateTime.now();
    }

    public void confirmEmail() {
        this.email = this.email.verify();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeAddress(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalArgumentException("O novo endereço não pode ser nulo.");
        }
        this.address = newAddress;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean login(String passwordEntered) {
        return this.passwordHash.verify(passwordEntered);
    }

    public void changePassword(String newPassword) {
        this.passwordHash = PasswordHash.createFromRaw(newPassword);
    }

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
