package tests.user;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import pojo.User;
import service.UserStep;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("PATCH /api/auth/user Изменение данных пользователя")
public class UserUpdateTest {

    private User user;
    private UserStep userStep;
    private String accessToken;

    @Before
    public void setUp() {
        userStep = new UserStep();
        user = new User(User.generateRandomEmail(), User.generateRandomPassword(), User.generateRandomName());
        userStep.setUser(user);
        accessToken = userStep.createUser().jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    @Description("Проверка, что email пользователя можно изменить с авторизацией")
    public void updateUserWithAuthorizationEmail() {
        user.setEmail(User.generateRandomEmail());
        userStep.setUser(user);
        userStep.updateUserWithToken(accessToken).then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Проверка, что имя пользователя можно изменить с авторизацией")
    public void updateUserWithAuthorizationName() {
        user.setName(User.generateRandomName());
        userStep.setUser(user);
        userStep.updateUserWithToken(accessToken).then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Попытка изменения email пользователя без авторизации")
    @Description("Проверка, что попытка изменения email без авторизации возвращает ошибку")
    public void updateUserWithoutAuthorizationEmail() {
        user.setEmail(User.generateRandomEmail());
        userStep.setUser(user);
        userStep.updateUserWithoutToken().then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Попытка изменения имени пользователя без авторизации")
    @Description("Проверка, что попытка изменения имени без авторизации возвращает ошибку")
    public void updateUserWithoutAuthorizationName() {
        user.setName(User.generateRandomName());
        userStep.setUser(user);
        userStep.updateUserWithoutToken().then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userStep.deleteUser(accessToken);
        }
    }
}
