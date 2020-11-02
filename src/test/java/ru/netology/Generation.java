package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@Data
@NoArgsConstructor
public class Generation {
    private static Faker faker = new Faker(new Locale("en"));

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void registred (Registration registration) {
        given()
                .spec(requestSpec)
                .body(registration)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static Registration newActiveValidUser() {
        String login = faker.name().firstName().toLowerCase();
        String password = faker.internet().password();
        registred(new Registration(login, password, "active"));
        return new Registration(login, password, "active");
    }

    public static Registration newBlockedUser() {
        String login = faker.name().firstName().toLowerCase();
        String password = faker.internet().password();
        registred(new Registration(login, password, "blocked"));
        return new Registration(login, password, "blocked");
    }

    public static Registration newActiveUserInvalidLogin() {
        String password = faker.internet().password();
        String status = "active";
        registred(new Registration("vasiliy", password, status));
        return new Registration("login", password, status);
    }

    public static Registration newActiveInvalidPassword() {
        String login = faker.name().firstName().toLowerCase();
        String status = "active";
        registred(new Registration(login, "password", status));
        return new Registration(login, "11111", status);
    }
}