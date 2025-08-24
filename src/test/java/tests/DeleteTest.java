package tests;

import apiTests.BookListApi;
import apiTests.LoginApi;
import com.codeborne.selenide.Selenide;
import models.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeleteTest extends TestBase {

    @Test
    public void deleteItemTest() {
        LoginBodyModel userData = new LoginBodyModel();
        LoginApi loginApi = new LoginApi();
        userData.setUserName("kate_smith");
        userData.setPassword("Katesmith9$");

        BookListApi bookApi = new BookListApi();
        AddListOfBooksBodyModel bookData = new AddListOfBooksBodyModel();

        LoginResponseModel loginResponse = step("Make login request", () ->
        loginApi.login(userData));

        step("Check login successful", () -> {
            loginApi.loginCheck(userData, loginResponse);
        });

        bookApi.addBookToISBNCollection(bookData, loginResponse);
        AddListOfBooksResponseModel bookResponse = step("Make request to add list of books to profile", () ->
            bookApi.bookAdd(bookData, loginResponse));

        step("Check books are added", () -> {
            bookApi.booksCheck(bookResponse);
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
        });

        step("Close browser confirmation window with UI", () -> {
            Selenide.confirm();
        });

        GetListOfBooksResponseModel userBookResponse = step("Make request to get a list of user's books", () ->
                loginApi.getUserBookResponse(loginResponse));

        step("Confirm removal with api by response", () -> {
            loginApi.usersBookListCheck(userData, loginResponse, bookResponse, userBookResponse);
        });
    }
}
