package com.danvinicius.simplifiedpicpay.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.domain.user.UserType;
import com.danvinicius.simplifiedpicpay.dto.UserDTO;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    private String document = "123456789-01";

    @Test
    @DisplayName("Should get user sucessfully from database")
    void testFindUserByDocumentSucess() {
        UserType type = UserType.valueOf("COMMON");
        BigDecimal balance = new BigDecimal(1000);
        UserDTO data = new UserDTO("dan", "vinic", this.document, balance, "dan@mail.com", "1234", type);
        this.createUser(data);

        Optional<User> result = this.userRepository.findUserByDocument(document);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user sucessfully from database")
    void testFindUserByDocumentFailure() {
        Optional<User> result = this.userRepository.findUserByDocument(this.document);
        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDTO user) {
        User newUser = new User(user);
        this.entityManager.persist(newUser);
        return newUser;
    }
}
