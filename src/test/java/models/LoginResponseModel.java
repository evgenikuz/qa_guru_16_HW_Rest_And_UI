package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponseModel {
    @JsonProperty("created_date")
    String createdDate;
    String expires, password, token, userId, username;
    Boolean isActive;
}
