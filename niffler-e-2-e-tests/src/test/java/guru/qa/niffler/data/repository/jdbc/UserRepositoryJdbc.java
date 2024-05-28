package guru.qa.niffler.data.repository.jdbc;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.repository.UserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

    private static final DataSource authDataSource = DataSourceProvider.dataSource(DataBase.AUTH);
    private static final DataSource userDataDataSource = DataSourceProvider.dataSource(DataBase.USERDATA);

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        try (Connection connection = authDataSource.getConnection()) {
            connection.setAutoCommit(false);
            try
                    (PreparedStatement userPs = connection.prepareStatement(
                            "INSERT INTO \"user\" (" +
                                    "username, password, enabled, account_non_expired, " +
                                    "account_non_locked, credentials_non_expired) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                     PreparedStatement authorityPs = connection.prepareStatement(
                             "INSERT INTO \"authority\" (" +
                                     "user_id, authority) " +
                                     "VALUES (?, ?)"
                     )) {

                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());

                userPs.executeUpdate();

                UUID generateUserId = null;
                try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generateUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("No access to id");
                    }
                }
                user.setId(generateUserId);

                for (Authority a :Authority.values()){
                    authorityPs.setObject(1, generateUserId);
                    authorityPs.setString(2, a.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }
                authorityPs.executeBatch();
                connection.commit();
                return user;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        try (Connection connection = userDataDataSource.getConnection()) {
            PreparedStatement userPs = connection.prepareStatement(
                    "INSERT INTO \"user\" (" +
                            "username, currency, firstname, surname, " +
                            "photo, photo_small) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            {
                userPs.setString(1, user.getUsername());
                userPs.setString(2, user.getCurrency().name());
                userPs.setString(3, user.getFirstname());
                userPs.setString(4, user.getSurname());
                userPs.setObject(5, user.getPhoto());
                userPs.setObject(6, user.getPhoto_small());

                userPs.executeUpdate();

                UUID generateUserId = null;
                try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generateUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("No access to id");
                    }
                }
                user.setId(generateUserId);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findUserInUserDataById(UUID id) {
        return Optional.empty();
    }
}
