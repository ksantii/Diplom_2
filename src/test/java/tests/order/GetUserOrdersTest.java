package tests.order;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojo.*;
import service.*;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("GET /api/orders Получить заказы конкретного пользователя")
public class GetUserOrdersTest {

    private User user;
    private Order order;
    private OrderStep orderStep;
    private String accessToken;
    private UserStep userStep;

    @Before
    public void setUp() {
        user = new User(User.generateRandomEmail(), User.generateRandomPassword(), User.generateRandomName());
        userStep = new UserStep();
        userStep.setUser(user);
        accessToken = userStep.createUser().jsonPath().getString("accessToken");

        List<String> ingredients = new OrderStep(null).getIngredients().subList(0, 3);
        order = new Order(ingredients);
        orderStep = new OrderStep(order);
        orderStep.createOrderWithToken(accessToken).then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверка, что авторизованный пользователь может получить список своих заказов")
    public void getUserOrdersWithAuthorization() {
        orderStep.getUserOrders(accessToken)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    @Description("Проверка, что нельзя получить список заказов без авторизации")
    public void getUserOrdersWithoutAuthorization() {
        given()
                .log().all()
                .baseUri(Endpoints.BASE_URI)
                .basePath(Endpoints.ORDERS)
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
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
