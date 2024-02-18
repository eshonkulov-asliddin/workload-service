package dev.gym.workloadservice.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String token;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil();
        jwtUtil.setSecret("2dae84f846e4f4b158a8d26681707f4338495bc7ab68151d7f7679cc5e56202dd3da0d356da007a7c28cb0b780418f4f3246769972d6feaa8f610c7d1e7ecf6a");
        token = jwtUtil.generateToken("username");
    }

    @Test
    void givenValidToken_whenValidate_thenOk(){
        // success - no exception thrown
        jwtUtil.validateToken(token);
    }

    @Test
    void givenInvalidToken_whenValidate_thenThrowException() {
        token += "invalid";
        assertThrows(Exception.class, () -> jwtUtil.validateToken(token));
    }

    @Test
    void givenValidToken_whenSecretIsChanged_thenThrowException() {
        jwtUtil.setSecret("invalid");
        assertThrows(Exception.class, () -> jwtUtil.validateToken(token));
    }
}
