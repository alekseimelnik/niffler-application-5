package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.hibernate.UserRepositoryHibernate;
import guru.qa.niffler.data.repository.jdbc.UserRepositoryJdbc;
import guru.qa.niffler.data.repository.springjdbc.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.annotation.User;

import java.util.Optional;
import java.util.UUID;

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

    Optional<UserEntity> findUserInUserDataById(UUID id);

    UserAuthEntity updateUserInAuth(UserAuthEntity user);

    UserEntity updateUserInUserdata(UserEntity user);
}
