package guru.qa.niffler.data.repository.hibernate;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {
    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        return null;
    }

    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        return null;
    }

    @Override
    public Optional<UserEntity> findUserInUserDataById(UUID id) {
        return Optional.empty();
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        return null;
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        return null;
    }
}
