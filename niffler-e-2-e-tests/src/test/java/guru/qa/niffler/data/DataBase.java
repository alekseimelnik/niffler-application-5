package guru.qa.niffler.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DataBase {
    AUTH("jdbc:postgresql://niffler-all-db:5432/niffler-auth"),
    CURRENCY("jdbc:postgresql://niffler-all-db:5432/niffler-currency"),
    SPEND("jdbc:postgresql://niffler-all-db:5432/niffler-spend"),
    USERDATA("jdbc:postgresql://niffler-all-db:5432/niffler-userdata");

    private final String jdbcUrl;

}
