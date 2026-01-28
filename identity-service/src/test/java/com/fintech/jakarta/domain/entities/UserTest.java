package com.fintech.jakarta.domain.entities;

import com.fintech.jakarta.domain.enuns.UserType;
import com.fintech.jakarta.domain.valueobject.Address;
import com.fintech.jakarta.domain.valueobject.CPF;
import com.fintech.jakarta.domain.valueobject.Email;
import com.fintech.jakarta.domain.valueobject.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    private Address createValidAddress() {
        return new Address("Rua Teste", "Bairro", "Cidade", "CE", "60000-000", "Brasil");
    }

    private User createValidUser() {
        return new User(
                "Carlos",
                "Dev",
                new Email("carlos@teste.com"),
                "Senha1234!",
                new CPF("132.438.340-23"),
                UserType.USER,
                new PhoneNumber("88999998888"),
                createValidAddress()
        );
    }

    @Test
    @DisplayName("Deve criar usuário ativo e com datas preenchidas (via PrePersist simulado)")
    void shouldCreateUserActive() {
        User user = createValidUser();

        assertTrue(user.isActive(), "Usuário deve nascer ativo");
        assertEquals("Carlos", user.getFirstName());
        assertEquals(UserType.USER, user.getUserType());

        invokePrePersist(user);

        assertNotNull(user.getId(), "ID deve ser gerado no onCreate");
        assertNotNull(user.getCreatedAt(), "Data de criação deve ser gerada");
    }

    @Test
    @DisplayName("Deve atualizar perfil com sucesso")
    void shouldUpdateProfile() {
        User user = createValidUser();

        user.updateProfile("Novo Nome", "Novo Sobrenome");

        assertEquals("Novo Nome", user.getFirstName());
        assertEquals("Novo Sobrenome", user.getLastName());
    }

    @Test
    @DisplayName("Não deve permitir atualizar perfil com nome inválido")
    void shouldThrowExceptionWhenUpdateProfileWithInvalidName() {
        User user = createValidUser();

        // Verifica se lança a exceção esperada
        assertThrows(IllegalArgumentException.class, () -> {
            user.updateProfile("", "Sobrenome");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user.updateProfile(null, "Sobrenome");
        });
    }

    @Test
    @DisplayName("Deve atualizar email e resetar verificação")
    void shouldUpdateEmail() {
        User user = createValidUser();

        user.confirmEmail();
        assertTrue(user.getEmail().isVerified());

        user.updateEmail("novo@email.com");

        assertEquals("novo@email.com", user.getEmail().getValue());
        assertFalse(user.getEmail().isVerified(), "Ao mudar o email, o status deve voltar para não verificado");
    }

    @Test
    @DisplayName("Não deve alterar nada se o email for o mesmo")
    void shouldNotChangeIfEmailIsSame() {
        User user = createValidUser();
        String originalEmail = user.getEmail().getValue();

        user.confirmEmail();

        user.updateEmail(originalEmail); // Tenta mudar para o mesmo

        assertTrue(user.getEmail().isVerified(), "Não deve perder verificação se o email é igual");
    }

    @Test
    @DisplayName("Deve realizar login com a senha correta")
    void shouldLoginWithCorrectPassword() {
        User user = createValidUser(); // Senha original: "senhaForte123"

        assertTrue(user.login("Senha1234!"), "Login deve ter sucesso com senha correta");
        assertFalse(user.login("Senha1235!"), "Login deve falhar com senha errada");
    }

    @Test
    @DisplayName("Deve alterar a senha e permitir login com a nova")
    void shouldChangePassword() {
        User user = createValidUser();

        user.changePassword("SenhaFort12!");

        assertTrue(user.login("SenhaFort12!"), "Deve logar com a nova senha");
        assertFalse(user.login("Senha1234!"), "Não deve logar com a antiga");
    }

    @Test
    @DisplayName("Deve alterar endereço corretamente")
    void shouldChangeAddress() {
        User user = createValidUser();
        Address newAddress = new Address("Rua Nova", "Bairro Novo", "Cidade Nova", "SP", "11000-000", "Brasil");

        user.changeAddress(newAddress);

        assertEquals(newAddress, user.getAddress());
    }

    @Test
    @DisplayName("Deve desativar e reativar usuário")
    void shouldToggleActiveStatus() {
        User user = createValidUser();

        user.deactivate();
        assertFalse(user.isActive());

        user.activate();
        assertTrue(user.isActive());
    }

    private void invokePrePersist(User user) {
        try {
            java.lang.reflect.Method method = User.class.getDeclaredMethod("onCreate");
            method.setAccessible(true);
            method.invoke(user);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao invocar onCreate via reflection para teste", e);
        }
    }
}