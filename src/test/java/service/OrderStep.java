package service;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pojo.Order;
import java.util.List;
import static io.restassured.RestAssured.given;

public class OrderStep {

    private final Order order;

    public OrderStep(Order order) {
        this.order = order;
    }

    @Step("Создать заказ с помощью авторизации")
    public Response createOrderWithToken(String token) {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .basePath(Endpoints.ORDERS)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(order)
                .when()
                .post();
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutToken() {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .basePath(Endpoints.ORDERS)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post();
    }

    @Step("Получить заказы пользователя с токеном")
    public Response getUserOrders(String token) {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .basePath(Endpoints.ORDERS)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .get();
    }

    @Step("Получить список ингредиентов")
    public List<String> getIngredients() {
        return given().log().all()
                .baseUri(Endpoints.BASE_URI)
                .basePath(Endpoints.INGREDIENTS)
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .extract().path("data._id");
    }
}
