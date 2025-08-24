package tests;

import com.codeborne.selenide.Selenide;
import models.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static specs.RequestSpec.requestSpec;
import static specs.ResponseSpec.responseSpec;

public class DeleteTest extends TestBase {

    @Test
    public void deleteItemTest() {
        LoginBodyModel userData = new LoginBodyModel();
        userData.setUserName("kate_smith");
        userData.setPassword("Katesmith9$");

        LoginResponseModel loginResponse = step("Make login request", () ->
                given(requestSpec)
                        .body(userData)

                        .when()
                        .post("/Account/v1/Login")

                        .then()
                        .spec(responseSpec(200))
                        .extract().as(LoginResponseModel.class));

        step("Check login successful", () -> {
            assertEquals(userData.getUserName(), loginResponse.getUsername());
            assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImthdGVfc21pdGgiLCJwYXNzd29yZCI6IkthdGVzbWl0aDkkIiwiaWF0IjoxNzU2MDMzNTYxfQ.jFb7sg2mXcTcaZjXODHXmKOZmfZAumaFXLYZao2S9Eg", loginResponse.getToken());
            assertEquals("450c031d-1bc6-4338-92a9-c5972049648c", loginResponse.getUserId());
        });

        AddListOfBooksBodyModel bookData = new AddListOfBooksBodyModel();
        bookData.setUserId(loginResponse.getUserId());
        List<CollectionOfIsbnsModel> isbnList = new ArrayList<>();
        CollectionOfIsbnsModel isbn1 = new CollectionOfIsbnsModel();
        isbn1.setIsbn("9781449325862");
        isbnList.add(isbn1);
        CollectionOfIsbnsModel isbn2 = new CollectionOfIsbnsModel();
        isbn2.setIsbn("9781449331818");
        isbnList.add(isbn2);
        bookData.setCollectionOfIsbns(isbnList);
        AddListOfBooksResponseModel bookResponse = step("Make request to add list of books to profile", () ->
                given(requestSpec)
                        .header("Authorization", "Bearer " + loginResponse.getToken())
                        .body(bookData)

                        .when()
                        .post("/BookStore/v1/Books")

                        .then()
                        .spec(responseSpec(201))
                        .extract().as(AddListOfBooksResponseModel.class));

        step("Check books are added", () -> {
            assertEquals("9781449325862", bookResponse.getBooks().get(0).getIsbn());
            assertEquals("9781449331818", bookResponse.getBooks().get(1).getIsbn());
        });

        step("Authorization with api", () -> {
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userName", loginResponse.getUsername()));
        getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));
        getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));
        });

        step("Open UI profile", () -> {
            open("/profile");
            executeJavaScript("$('footer').remove();");
            executeJavaScript("$('#fixedban').remove();");
            $("#userName-value").shouldHave(text(userData.getUserName()));
        });

        step("Click delete icon with UI", () -> {
            $$(".mr-2").findBy(text("Learning JavaScript Design Patterns")).closest(".rt-tr").$("#delete-record-undefined").click();
        });

        step("Confirm removal of a book with UI", () -> {
            $("#closeSmallModal-ok").click();
            Selenide.confirm();
        });

        GetListOfBooksResponseModel userBookResponse = step("Make request to get a list of user's books", () ->
                given(requestSpec)
                        .header("Authorization", "Bearer " + loginResponse.getToken())

                        .when()
                        .get("/Account/v1/User/" + loginResponse.getUserId())

                        .then()
                        .spec(responseSpec(200))
                        .extract().as(GetListOfBooksResponseModel.class));

        step("Confirm removal with api", () -> {
            assertEquals(loginResponse.getUserId(), userBookResponse.getUserId());
            assertEquals(userData.getUserName(), userBookResponse.getUsername());
            assertEquals(bookResponse.getBooks().get(0).getIsbn(), userBookResponse.getBooks().get(0).getIsbn());
            assertFalse(userBookResponse.getBooks().stream().anyMatch(books -> bookResponse.getBooks().get(1).getIsbn().equals(books.getIsbn())));
        });
    }
}
