package com.fintech.jakarta.application.dto.command.users;

import com.fintech.jakarta.domain.valueobject.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEventDTO {
    private String id;
    private String firstName;
    private String email;
    private Address address;
    private String cpf;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}