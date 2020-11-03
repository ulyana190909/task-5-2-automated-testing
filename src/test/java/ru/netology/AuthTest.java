package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLogInIfActiveValidUser() {
        Registration user = Generation.newActiveValidUser();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    void shouldNotLogInIfBlockedUser() {
        Registration user = Generation.newBlockedUser();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Пользователь заблокирован")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldNotLogInIfActiveUserInvalidLogin() {
        Registration user = Generation.newActiveUserInvalidLogin();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Неверно указан логин или пароль")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldNotLogInIfActiveUserInvalidPassword() {
        Registration user = Generation.newActiveInvalidPassword();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Неверно указан логин или пароль")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldNotLogInIfActiveUserEmptyLogin() {
        Registration user = Generation.newActiveValidUser();
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='login'] .input__sub").shouldHave(text("Поле обязательно для заполнения")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldNotLogInIfActiveUserEmptyPassword() {
        Registration user = Generation.newActiveValidUser();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='password'] .input__sub").shouldHave(text("Поле обязательно для заполнения")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldNotEnterTheSystemIfTheUserHasNotEnteredData() {
        $$("[class='button__text']").find(exactText("Продолжить")).click();
        $("[data-test-id='login'] .input__sub").shouldHave(text("Поле обязательно для заполнения")).waitUntil(Condition.visible, 15000);
        $("[data-test-id='password'] .input__sub").shouldHave(text("Поле обязательно для заполнения")).waitUntil(Condition.visible, 15000);
    }

}
