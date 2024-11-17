package pojo;

import com.github.javafaker.Faker;

public class User {
    private String email;
    private String password;
    private String name;

    private static final Faker faker = new Faker();

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User() {
    }

    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateRandomPassword() {
        return faker.internet().password();
    }

    public static String generateRandomName() {
        return faker.name().firstName();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
