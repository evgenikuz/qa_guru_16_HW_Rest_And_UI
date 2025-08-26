package ui;

import models.AddListOfBooksResponseModel;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.openqa.selenium.Cookie;
import pages.ProfilePage;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;

public class DeleteUI {
    ProfilePage profilePage = new ProfilePage();
    public void DeleteBookWithUI(LoginResponseModel loginResponse, LoginBodyModel userData, AddListOfBooksResponseModel bookResponse) {
        step("Authorization with api", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userName", loginResponse.getUsername()));
            getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));
            getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));
        });

        profilePage.openPage(userData)
                .removeAds()
                .clickOnBinIcon("Learning JavaScript Design Patterns")
                .clickOnOkButton()
                .closeConfirmationWindow();
    }
}
