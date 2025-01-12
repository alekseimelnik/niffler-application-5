package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("photoSmall")
        String photoSmall,
        @JsonProperty("friendState")
        FriendState friendState,
        @JsonIgnore
        TestData testData) {

    public UserJson(String username) {
        this(username, null);
    }

    public UserJson(String username, TestData testData) {
        this(null, username, null, null, null, null, null, null, testData);
    }

    public UserJson addTestData(TestData testData) {
        return new UserJson(id, username, firstname, surname, currency, photo, photoSmall, friendState, testData);
    }

    public static UserJson randomUser() {
        Faker faker = new Faker();
        return new UserJson(
                null,
                faker.name().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.options().option(CurrencyValues.class),
                null,
                null,
                null,
                new TestData(
                        faker.internet().password()
                )
        );
    }
}
