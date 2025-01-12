package guru.qa.niffler.data.repository.jdbc;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final DataSource spendDataSource = DataSourceProvider.dataSource(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO \"category\" (category, username) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.executeUpdate();

            UUID generateId = null;
            try(ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generateId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("No access to id");
                }
            }
            category.setId(generateId);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE category SET category = ?, username = ? WHERE id = ?"
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.setObject(3,category.getId());

            ps.executeUpdate();
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM \"category\" WHERE id = ?"
             )){
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO spend (username, currency, spend_date, amount, description, category_id) " +
                             "VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, spend.getUsername());
            ps.setString(2, spend.getCurrency().name());
            ps.setDate(3, new Date(spend.getSpendDate().getTime()));
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6,spend.getCategory());

            ps.executeUpdate();

            UUID generateId = null;
            try(ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generateId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("No access to id");
                }
            }
            spend.setId(generateId);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE spend SET username = ?, currency = ?, spend_date = ?, amount = ?," +
                             "description = ?, category_id = ? WHERE id = ?"
             )) {
            ps.setString(1, spend.getUsername());
            ps.setString(2, spend.getCurrency().name());
            ps.setDate(3, new Date(spend.getSpendDate().getTime()));
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6,spend.getCategory());
            ps.setObject(7,spend.getId());

            ps.executeUpdate();
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM spend WHERE id = ?"
             )) {
            ps.setObject(1,spend.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> spends = new ArrayList<>();
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM spend WHERE id = ?")) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                SpendEntity spend = new SpendEntity();
                spend.setId(UUID.fromString(resultSet.getString("id")));
                spend.setUsername(resultSet.getString("username"));
                spend.setSpendDate(resultSet.getDate("spend_date"));
                spend.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                spend.setAmount(resultSet.getDouble("amount"));
                spend.setDescription(resultSet.getString("description"));
                spend.setCategory(resultSet.getString("category"));
                spends.add(spend);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spends;
    }
}