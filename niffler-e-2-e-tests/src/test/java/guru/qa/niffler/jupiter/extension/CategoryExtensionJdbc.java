package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtensionJdbc implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryExtensionJdbc.class);

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                cat -> {

                        CategoryEntity category = new CategoryEntity();
                        category.setCategory(cat.category());
                        category.setUsername(cat.username());

                        category = spendRepository.createCategory(category);
                        extensionContext.getStore(NAMESPACE).put(
                                extensionContext.getUniqueId(), CategoryJson.fromEntity(category)
                        );
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        CategoryJson categoryJson = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
        spendRepository.removeCategory(CategoryEntity.ftomJson(categoryJson));
    }
}
