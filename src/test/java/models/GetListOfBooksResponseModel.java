package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetListOfBooksResponseModel {
    String userId, username;
    List<Books> books;
    @Data
    public static class Books {
        String isbn, title, subTitle, author, publisher, description, website;
        @JsonProperty("publish_date")
        String publishDate;
        Integer pages;
    }
}
