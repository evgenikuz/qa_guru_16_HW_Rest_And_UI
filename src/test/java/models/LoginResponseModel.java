package models;

import lombok.Data;

@Data
public class LoginResponseModel {
    String created_date, expires, password, token, userId, username;
    Boolean isActive;
}
