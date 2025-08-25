package tests;

import apiTests.BookListApi;
import apiTests.LoginApi;
import models.*;
import org.junit.jupiter.api.Test;
import uiTests.DeleteUI;

import static io.qameta.allure.Allure.step;

public class DemoQATest extends TestBase {

    @Test
    public void deleteOneOfItemsTest() {
        LoginBodyModel userData = new LoginBodyModel();
        LoginApi loginApi = new LoginApi();
        userData.setUserName("kate_smith");
        userData.setPassword("Katesmith9$");

        BookListApi bookApi = new BookListApi();
        AddListOfBooksBodyModel bookData = new AddListOfBooksBodyModel();

        DeleteUI deleteUI = new DeleteUI();

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

        step("Delete a book with UI", () -> {
            deleteUI.DeleteBookWithUI(loginResponse, userData);
        });

        GetListOfBooksResponseModel userBookResponse = step("Make request to get a list of user's books", () ->
                loginApi.getUserBookResponse(loginResponse));

        step("Confirm removal with api by response", () -> {
            loginApi.usersBookListCheck(userData, loginResponse, bookResponse, userBookResponse);
        });
    }
}
