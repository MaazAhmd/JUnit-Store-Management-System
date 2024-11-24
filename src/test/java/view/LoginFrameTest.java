package view;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

class LoginFrameTest {

    private LoginFrame loginFrame;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize the LoginFrame
        loginFrame = new LoginFrame();
    }

    // Parameterized test to check different username and password combinations
    @ParameterizedTest
    @CsvSource({
            "admin, test, true",         // Correct username and password
            "admin, wrongPassword, false", // Correct username, wrong password
            "invalidUser, anyPassword, false" // Invalid username
    })
    void testLoginAuthentication(String username, String password, boolean expectedResult) {
        // Simulate the login process by directly comparing username and password

        // Set the username and password in the login form
        loginFrame.usernameTextField.setText(username);
        loginFrame.passwordPasswordField.setText(password);

        // Perform login authentication (checking directly in the method)
        boolean result = loginFrame.loginAuthentication();

        // Assert that the result matches the expected outcome
        assertEquals(expectedResult, result);
    }
}
