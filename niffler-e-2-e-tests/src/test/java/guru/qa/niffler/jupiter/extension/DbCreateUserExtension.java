package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.springjdbc.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.extension.abstr.AbstractCreateUserExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

public class DbCreateUserExtension extends AbstractCreateUserExtension {
    UserRepository userRepository = new UserRepositorySpringJdbc();

    @Override
    protected UserJson createUser(UserJson user) {
        userRepository.createUserInAuth(UserAuthEntity.fromJson(user));
        userRepository.createUserInUserData(UserEntity.fromJson(user));
        return user;
    }
}
