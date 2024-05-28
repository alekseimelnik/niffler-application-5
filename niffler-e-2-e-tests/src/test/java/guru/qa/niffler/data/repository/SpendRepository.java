package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.jdbc.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.springjdbc.SpendRepositorySpringJdbc;

public interface SpendRepository {

    static SpendRepository getInstance() {
        return switch (System.getProperty("repo")) {
            case "sjdbc" -> new SpendRepositorySpringJdbc();
            case "hibernate" -> new SpendRepositoryHibernate();
            default -> new SpendRepositoryJdbc();
        };
    }

    CategoryEntity createCategory(CategoryEntity category);

    CategoryEntity editCategory(CategoryEntity category);

    void removeCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity spend);

    SpendEntity editSpend(SpendEntity spend);

    void removeSpend(SpendEntity spend);

}
