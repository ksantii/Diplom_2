package service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import io.restassured.http.ContentType;
import pojo.User;
import static io.restassured.RestAssured.given;

public class UserStep {
    private User user;
    private User userLogin;

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    @Step("Создать пользователя")
    public Response createUser() {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(Endpoints.REGISTER);
    }

    @Step("Войти под пользователем")
    public Response loginUser() {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post(Endpoints.LOGIN);
    }

    @Step("Получить токен доступа")
    public String getAccessToken() {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .extract().path("accessToken");
    }

    @Step("Обновить пользователя с токеном")
    public Response updateUserWithToken(String token) {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(Endpoints.USER);
    }

    @Step("Обновить пользователя без токена")
    public Response updateUserWithoutToken() {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(Endpoints.USER);
    }

    @Step("Удалить пользователя")
    public void deleteUser(String token) {
        given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .header("Authorization", token)
                .when()
                .delete(Endpoints.USER)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }
}
