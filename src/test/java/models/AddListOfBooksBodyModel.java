package models;

import lombok.Data;

import java.util.List;

@Data
public class AddListOfBooksBodyModel {
    String userId;
    List<CollectionOfIsbnsModel> collectionOfIsbns;
}
