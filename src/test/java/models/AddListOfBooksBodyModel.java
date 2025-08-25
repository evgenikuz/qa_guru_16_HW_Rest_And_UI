package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class AddListOfBooksBodyModel {
    String userId;
    List<CollectionOfIsbnsModel> collectionOfIsbns;
}
