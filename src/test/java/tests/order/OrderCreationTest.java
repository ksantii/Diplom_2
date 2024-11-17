package tests.order;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojo.Order;
import service.OrderStep;
import pojo.User;
import service.UserStep;
import static org.hamcrest.Matchers.*;
import java.util.List;

@DisplayName("POST /api/orders Создание заказа")
public class OrderCreationTest {

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
        orderStep = new OrderStep(null);
        order = new Order(orderStep.getIngredients().subList(0, 4));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка, что заказ можно создать с авторизацией")
    public void createOrderWithAuthorization() {
        new OrderStep(order)
                .createOrderWithToken(accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка, что заказ нельзя создать без авторизации")
    public void createOrderWithoutAuthorization() {
        new OrderStep(order)
                .createOrderWithoutToken()
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка, что заказ можно создать с валидными ингредиентами")
    public void createOrderWithIngredients() {
        List<String> ingredients = new OrderStep(null).getIngredients().subList(0, 3);
        new OrderStep(new Order(ingredients))
                .createOrderWithToken(accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка, что заказ нельзя создать без ингредиентов")
    public void createOrderWithoutIngredients() {
        new OrderStep(new Order())
                .createOrderWithToken(accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка, что заказ нельзя создать с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHashes() {
        List<String> invalidIngredients = List.of("invalidIngredientId1", "invalidIngredientId2");
        new OrderStep(new Order(invalidIngredients))
                .createOrderWithToken(accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userStep.deleteUser(accessToken);
        }
    }
}
