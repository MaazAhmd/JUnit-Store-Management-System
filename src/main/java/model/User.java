package model;

public class User {
    private String username;
    private String fullName;
    private String password;
    private String email;
    private String accessLevel;

    // Constructor
    public User(String username, String fullName, String password, String email, String accessLevel) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.accessLevel = accessLevel;
    }

    public User(String username, String correctPassword) {
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    // toString Method
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                '}';
    }
}
