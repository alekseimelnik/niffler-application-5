package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    public SpendEntityRowMapper() {

    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spendEntity = new SpendEntity();

        spendEntity.setId((UUID) rs.getObject("id"));
        spendEntity.setUsername(rs.getString("username"));
        spendEntity.setSpendDate(rs.getDate("spend_date"));
        spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spendEntity.setAmount(rs.getDouble("amount"));
        spendEntity.setDescription(rs.getString("description"));
        spendEntity.setCategory(String.valueOf(UUID.fromString(rs.getString("category_id"))));
        return spendEntity;
    }
}
