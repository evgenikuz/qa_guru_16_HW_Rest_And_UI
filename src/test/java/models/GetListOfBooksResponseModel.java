package models;

import lombok.Data;

import java.util.List;

@Data
public class GetListOfBooksResponseModel {
    String userId, username;
    List<Books> books;
    @Data
    public static class Books {
        String isbn, title, subTitle, author, publish_date, publisher, description, website;
        Integer pages;
    }
}
