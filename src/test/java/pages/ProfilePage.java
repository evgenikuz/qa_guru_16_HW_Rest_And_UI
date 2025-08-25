package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import models.LoginBodyModel;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

public class ProfilePage {
    public SelenideElement userName = $("#userName-value"),
            OkButton = $("#closeSmallModal-ok");
    public ElementsCollection bookNames = $$(".mr-2");
    public String deletedRow = ".rt-tr",
                 binIcon = "#delete-record-undefined";

    public ProfilePage openPage(LoginBodyModel userData) {
        step("Open UI profile", () -> {
            open("/profile");
            userName.shouldHave(text(userData.getUserName()));
        });
        return this;
    }

    public ProfilePage removeAds() {
    step("Remove ads", () -> {
        executeJavaScript("$('footer').remove();");
        executeJavaScript("$('#fixedban').remove();");
    });
        return this;
    }

    public ProfilePage clickOnBinIcon(String deletedBookTitle) {
        step("Click delete icon with UI", () -> {
            bookNames.findBy(text(deletedBookTitle)).closest(deletedRow).$(binIcon).click();
        });
        return this;
    }

    public ProfilePage clickOnOkButton() {
        step("Confirm removal of a book with UI", () -> {
        OkButton.click();
    });
        return this;
    }
    public ProfilePage closeConfirmationWindow() {
    step("Close browser confirmation window with UI", () -> {
        Selenide.confirm();
    });
        return this;
    }
}
