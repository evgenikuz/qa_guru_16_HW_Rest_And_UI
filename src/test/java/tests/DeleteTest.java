package tests;

import io.restassured.http.Cookies;
import models.LoginResponseModel;
import models.LoginBodyModel;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.devtools.v135.network.Network.setCookie;
import static specs.RequestSpec.requestSpec;
import static specs.ResponseSpec.responseSpec;

public class DeleteTest extends TestBase {

    @Test
    public void deleteItemTest() {
        LoginBodyModel userData = new LoginBodyModel();
        userData.setUserName("kate_smith");
        userData.setPassword("Katesmith9$");
        LoginResponseModel response = step("Make request", () ->
                given(requestSpec)
                        .body(userData)

                        .when()
                        .post("/Account/v1/Login")

                        .then()
                        .spec(responseSpec(200))
                        .extract().as(LoginResponseModel.class));

        step("Check response", () -> {
            assertEquals(userData.getUserName(), response.getUsername());
            assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImthdGVfc21pdGgiLCJwYXNzd29yZCI6IkthdGVzbWl0aDkkIiwiaWF0IjoxNzU2MDMzNTYxfQ.jFb7sg2mXcTcaZjXODHXmKOZmfZAumaFXLYZao2S9Eg", response.getToken());
            assertEquals("450c031d-1bc6-4338-92a9-c5972049648c", response.getUserId());
        });

        step("Authorization with api", () -> {
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userName", response.getUsername()));
        getWebDriver().manage().addCookie(new Cookie("userID", response.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("token", response.getToken()));
        getWebDriver().manage().addCookie(new Cookie("expires", response.getExpires()));
        });

        open("/profile");
        executeJavaScript("$('footer').remove();");
        executeJavaScript("$('#fixedban').remove();");
        $("#userName-value").shouldHave(text(userData.getUserName()));

    }
}
