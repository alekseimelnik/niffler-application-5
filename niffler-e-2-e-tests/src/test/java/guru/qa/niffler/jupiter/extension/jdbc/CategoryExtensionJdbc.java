package guru.qa.niffler.jupiter.extension.jdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.jdbc.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.abstr.AbstractCategoryExtension;
import guru.qa.niffler.model.CategoryJson;

public class CategoryExtensionJdbc extends AbstractCategoryExtension {

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    @Override
    protected CategoryJson createCategory(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory(category.category());
        categoryEntity.setUsername(category.username());

        categoryEntity = spendRepository.createCategory(categoryEntity);
        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        CategoryJson categoryJson = new CategoryJson(category.id(), category.category(), category.username());
        spendRepository.removeCategory(CategoryEntity.fromJson(categoryJson));
    }
}
