package guru.qa.niffler.jupiter.extension.jdbc;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.extension.abstr.AbstractSpendExtension;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Date;

public class SpendExtensionJdbc extends AbstractSpendExtension {

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    @Override
    protected SpendJson createSpend(ExtensionContext extensionContext, Spend spend) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setSpendDate(new Date());
        spendEntity.setCategory(spend.category());
        spendEntity.setAmount(spend.amount());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setDescription(spend.description());
        spendEntity.setUsername(spend.username());

        spendEntity = spendRepository.createSpend(spendEntity);

        return SpendJson.fromEntity(spendEntity);
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        spendRepository.removeSpend(SpendEntity.fromJson(spend));
    }
}
