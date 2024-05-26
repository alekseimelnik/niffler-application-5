package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

public interface UserRepository {

    static UserRepository getInstance() {
        return switch (System.getProperty("repo")) {
            case "sjdbc" -> new UserRepositorySpringJdbc();
            case "hibernate" -> new UserRepositoryHibernate();
            default -> new UserRepositoryJdbc();
        };
    }

    UserAuthEntity createUserInAuth(UserAuthEntity user);

    UserEntity createUserInUserData(UserEntity user);

}
