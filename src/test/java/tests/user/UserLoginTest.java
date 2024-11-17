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

@DisplayName("POST /api/auth/login Логин пользователя")
public class UserLoginTest {

    private User user;
    private UserStep userStep;
    private String accessToken;

    @Before
    public void setUp() {
        user = new User(User.generateRandomEmail(), User.generateRandomPassword(), User.generateRandomName());
        userStep = new UserStep();
        userStep.setUser(user);
        accessToken = userStep.createUser().jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    @Description("Проверка, что можно успешно авторизоваться под существующим пользователем")
    public void loginWithValidUser() {
        userStep.setUserLogin(user);
        userStep.loginUser().then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    @Description("Проверка, что авторизация с неверным email возвращает ошибку")
    public void loginWithInvalidEmail() {
        user.setEmail("email@emailcom");
        userStep.setUserLogin(user);
        userStep.loginUser().then().log().all()
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
