package com.example.itera.repository.user;

import com.example.itera.domain.user.User;
import com.example.itera.domain.user.UserRole;
import com.example.itera.dto.user.RegisterDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from database")
    void findByLoginSuccess() {
        String login = "mars123";
        RegisterDTO data = new RegisterDTO("Marcus", login, "marcus@itera.com.br", "teste123", UserRole.ADMIN);
        this.createUser(data);
        UserDetails result = this.userRepository.findByLogin(login);
        assertThat(result.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Should not get User from database when user not exist")
    void findByLoginError() {
        String login = "mars123";
        UserDetails result = this.userRepository.findByLogin(login);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should get User successfully from database by username")
    void findByNomeSuccess() {
        String username = "mars123";
        RegisterDTO data = new RegisterDTO("Marcus", username, "marcus@itera.com.br", "teste123", UserRole.ADMIN);
        User user = createUser(data);

        User result = userRepository.findByNome(username);

        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo(username);
    }

    @Test
    @DisplayName("Should not get User from database when user with username not exist")
    void findByNomeError() {
        String name = "NonExistingUser";
        User result = userRepository.findByNome(name);

        assertThat(result).isNull();
    }
    private User createUser(RegisterDTO data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}