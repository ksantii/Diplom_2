package tests.user;

import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import pojo.User;
import service.UserStep;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("POST /api/auth/register Создание пользователя")
public class UserRegistrationTest {

    private User userCreate;
    private UserStep userStep;
    private String accessToken;

    @Before
    public void setUp() {
        userCreate = new User(User.generateRandomEmail(), User.generateRandomPassword(), User.generateRandomName());
        userStep = new UserStep();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка, что пользователь может быть успешно создан")
    public void createUniqueUser() {
        userStep.setUser(userCreate);
        Response response = userStep.createUser();
        accessToken = response.jsonPath().getString("accessToken");
        response.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }


    @Test
    @DisplayName("Создание пользователя с уже существующими данными")
    @Description("Проверка, что попытка создать пользователя с уже зарегистрированным email возвращает ошибку")
    public void createUserWithExistingEmail() {
        userStep.setUser(userCreate);
        userStep.createUser().jsonPath().getString("accessToken");
        userStep.createUser().then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя с пустым email")
    @Description("Проверка, что попытка создать пользователя с пустым email возвращает ошибку")
    public void createUserWithEmptyEmail() {
        userCreate.setEmail("");
        userStep.setUser(userCreate);
        userStep.createUser().then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя с пустым паролем")
    @Description("Проверка, что попытка создать пользователя с пустым паролем возвращает ошибку")
    public void createUserWithEmptyPassword() {
        userCreate.setPassword("");
        userStep.setUser(userCreate);
        userStep.createUser().then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя с пустым именем")
    @Description("Проверка, что попытка создать пользователя с пустым именем возвращает ошибку")
    public void createUserWithEmptyName() {
        userCreate.setName("");
        userStep.setUser(userCreate);
        userStep.createUser().then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя с отсутствующим email")
    @Description("Проверка, что попытка создать пользователя с отсутствующим email возвращает ошибку")
    public void createUserWithMissingEmail() {
        userCreate.setEmail(null);
        userStep.setUser(userCreate);
        userStep.createUser().then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя с отсутствующим паролем")
    @Description("Проверка, что попытка создать пользователя с отсутствующим паролем возвращает ошибку")
    public void createUserWithMissingPassword() {
        userCreate.setPassword(null);
        userStep.setUser(userCreate);
        userStep.createUser().then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя с отсутствующим именем")
    @Description("Проверка, что попытка создать пользователя с отсутствующим именем возвращает ошибку")
    public void createUserWithMissingName() {
        userCreate.setName(null);
        userStep.setUser(userCreate);
        userStep.createUser().then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userStep.deleteUser(accessToken);
        }
    }
}