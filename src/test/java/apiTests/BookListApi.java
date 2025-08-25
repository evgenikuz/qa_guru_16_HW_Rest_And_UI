package apiTests;

import models.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.RequestSpec.requestSpec;
import static specs.ResponseSpec.responseSpec;

public class BookListApi {
    public void addBookToISBNCollection(AddListOfBooksBodyModel bookData, LoginResponseModel loginResponse) {
//        AddListOfBooksBodyModel bookData = new AddListOfBooksBodyModel();
        bookData.setUserId(loginResponse.getUserId());
        List<CollectionOfIsbnsModel> isbnList = new ArrayList<>();
        CollectionOfIsbnsModel isbn1 = new CollectionOfIsbnsModel();
        isbn1.setIsbn("9781449325862");
        isbnList.add(isbn1);
        CollectionOfIsbnsModel isbn2 = new CollectionOfIsbnsModel();
        isbn2.setIsbn("9781449331818");
        isbnList.add(isbn2);
        bookData.setCollectionOfIsbns(isbnList);
    }

    public AddListOfBooksResponseModel bookAdd(AddListOfBooksBodyModel bookData, LoginResponseModel loginResponse) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + loginResponse.getToken())
                .body(bookData)

                .when()
                .post("/BookStore/v1/Books")

                .then()
                .spec(responseSpec(201))
                .extract().as(AddListOfBooksResponseModel.class);
    }

    public void booksCheck(AddListOfBooksResponseModel bookResponse) {
        assertEquals("9781449325862", bookResponse.getBooks().get(0).getIsbn());
        assertEquals("9781449331818", bookResponse.getBooks().get(1).getIsbn());
    }
}
