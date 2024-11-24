package view;

import static org.junit.jupiter.api.Assertions.*;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ManageUsersFrameTest {
    private ManageUsersFrame manageUsersFrame;

    @BeforeEach
    void setUp() throws SQLException {
        manageUsersFrame = new ManageUsersFrame();
    }
    @ParameterizedTest
    @CsvSource({
            "acaminha, Alexandre, test123, alexandrencaminha@g..., Manager, true", // Valid user
            "'', Alexandre, test123, alexandrencaminha@g..., Manager, false",      // Empty username
            "acaminha, , test123, alexandrencaminha@g..., Manager, false",         // Empty fullName
            "acaminha, Alexandre, , alexandrencaminha@g..., Manager, false",       // Empty password
            "acaminha, Alexandre, test123, , Manager, false"                       // Empty email
    })
    void testDbCreateUser(String username, String fullName, String password, String email, String role, boolean expected) {
        User user = new User(username, fullName, password, email, role);

        boolean isCreated = manageUsersFrame.dbCreateUser(user);
        assertEquals(expected, isCreated, "User creation result should match the expected value.");

        int expectedSize = expected ? 1 : 0;
        assertEquals(expectedSize, manageUsersFrame.getAllUsers().size(), "User list size should match the expected value.");
    }
    @ParameterizedTest
    @MethodSource("provideUpdateUserTestData")
    void testDbUpdateUser(String existingUsername, User updatedUser, boolean expected) {
        // Perform the update operation
        boolean isUpdated = manageUsersFrame.dbUpdateUser(existingUsername, updatedUser);

        // Verify if the result matches the expected value
        assertEquals(expected, isUpdated, "Update result should match the expected value.");
    }


    private static Stream<Arguments> provideUpdateUserTestData() {
        return Stream.of(
                Arguments.of("maaz", new User("acaminha", "Alexandre", "newpassword", "alexandrencaminha@g...", "Manager"), true),
                Arguments.of("unknown_user", new User("acaminha", "Alexandre", "newpassword", "alexandrencaminha@g...", "Manager"), false),
                Arguments.of("maaz", new User("acaminha", "", "newpassword", "alexandrencaminha@g...", "Manager"), true) // Empty fullName allowed
        );
    }
    @ParameterizedTest
    @CsvSource({
            "acaminha, true",    // User exists
            "unknown_user, false" // User does not exist
    })
    @TestFactory
    Stream<DynamicTest> testDbDeleteUser() {
        return Stream.of(
                DynamicTest.dynamicTest("Delete existing user", () -> {
                    boolean isDeleted = manageUsersFrame.dbDeleteUser("maaz");
                    assertTrue(isDeleted, "User should be deleted successfully.");
                }),
                DynamicTest.dynamicTest("Delete non-existing user", () -> {
                    boolean isDeleted = manageUsersFrame.dbDeleteUser("unknown_user");
                    assertFalse(isDeleted, "User deletion should fail for non-existing user.");
                })
        );
    }
}
