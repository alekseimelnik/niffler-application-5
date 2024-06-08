package guru.qa.niffler.data.repository.jdbc;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.*;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.*;
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
                userPs.setObject(6, user.getPhotoSmall());

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
        UserEntity userEntity = new UserEntity();
        try (Connection connection = userDataDataSource.getConnection();
            PreparedStatement userPs = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE id = ?")) {
                userPs.setObject(1, id);
                try (ResultSet resultSet = userPs.executeQuery()){
                    if (resultSet.next()) {
                        userEntity.setId((UUID)resultSet.getObject("id"));
                        userEntity.setUsername(resultSet.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                        userEntity.setFirstname(resultSet.getString("firstname"));
                        userEntity.setSurname(resultSet.getString("surname"));
                        userEntity.setPhoto(resultSet.getBytes("photo"));
                        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
                    } else {
                        return Optional.empty();
                    }
                }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(userEntity);
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        try (Connection connection = authDataSource.getConnection()) {
            connection.setAutoCommit(false);
            try
                    (PreparedStatement userPs = connection.prepareStatement(
                            "UPDATE \"user\" SET username=?, password=?, enabled=?, account_non_expired=?," +
                                    "account_non_locked=?, credentials_non_expired=?) " +
                                    "WHERE id=?",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                     PreparedStatement deleteAuthorityPs = connection.prepareStatement(
                             "DELETE FROM \"authority\" WHERE user_id=?"
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
                userPs.setObject(7, user.getId());

                userPs.executeUpdate();

                deleteAuthorityPs.setObject(1, user.getId());
                deleteAuthorityPs.executeUpdate();

                for (AuthorityEntity a : user.getAuthorities()) {
                    authorityPs.setObject(1, user.getId());
                    authorityPs.setString(2, a.getAuthority().name());
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
    public UserEntity updateUserInUserdata(UserEntity user) {
        try (Connection connection = userDataDataSource.getConnection();
             PreparedStatement userPs = connection.prepareStatement(
                     "UPDATE \"user\" SET username=?, currency=?," +
                             "firstname=?, surname=?, photo=?," +
                             "photo_small=? WHERE id=?"
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
