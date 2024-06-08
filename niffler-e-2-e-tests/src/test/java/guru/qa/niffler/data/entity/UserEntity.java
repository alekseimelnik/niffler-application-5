package guru.qa.niffler.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import guru.qa.niffler.model.UserJson;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UserEntity implements Serializable {

    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private byte[] photo;
    private byte[] photo_small;

    public static UserEntity fromJson(UserJson userJson) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userJson.username());
        userEntity.setCurrency(CurrencyValues.valueOf(userJson.currency().name()));
        userEntity.setFirstname(userJson.firstname());
        userEntity.setSurname(userJson.surname());
        return userEntity;
    }
}