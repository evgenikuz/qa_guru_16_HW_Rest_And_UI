package ui;

import com.codeborne.selenide.Selenide;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;

public class DeleteUI {
    public void DeleteBookWithUI(LoginResponseModel loginResponse, LoginBodyModel userData) {
        step("Authorization with api", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userName", loginResponse.getUsername()));
            getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));
            getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));
        });

        step("Open UI profile", () -> {
            open("/profile");
            $("#userName-value").shouldHave(text(userData.getUserName()));
        });

        step("Remove ads", () -> {
            executeJavaScript("$('footer').remove();");
            executeJavaScript("$('#fixedban').remove();");
        });

        step("Click delete icon with UI", () -> {
            $$(".mr-2").findBy(text("Learning JavaScript Design Patterns")).closest(".rt-tr").$("#delete-record-undefined").click();
        });

        step("Confirm removal of a book with UI", () -> {
            $("#closeSmallModal-ok").click();
        });

        step("Close browser confirmation window with UI", () -> {
            Selenide.confirm();
        });
    }
}
