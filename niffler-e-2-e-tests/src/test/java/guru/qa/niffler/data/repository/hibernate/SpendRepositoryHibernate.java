package guru.qa.niffler.data.repository.hibernate;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import java.util.List;

public class SpendRepositoryHibernate implements SpendRepository {
    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return null;
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        return null;
    }

    @Override
    public void removeCategory(CategoryEntity category) {

    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        return null;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        return null;
    }

    @Override
    public void removeSpend(SpendEntity spend) {

    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return List.of();
    }
}
